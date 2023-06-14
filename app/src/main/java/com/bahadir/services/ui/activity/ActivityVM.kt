package com.bahadir.services.ui.activity

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahadir.core.common.ServiceName
import com.bahadir.core.common.collectIn
import com.bahadir.core.domain.provider.PermissionProvider
import com.bahadir.core.domain.provider.ResourceProvider
import com.bahadir.core.domain.usecase.GetServiceUseCase
import com.bahadir.core.domain.usecase.SetServiceUseCase
import com.bahadir.service.presentation.background.SensorService
import com.bahadir.service.presentation.bound.SoundService
import com.bahadir.services.R
import com.bahadir.services.delegation.viewmodel.VMDelegation
import com.bahadir.services.delegation.viewmodel.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityVM @Inject constructor(
    private val setServiceStatusUseCase: SetServiceUseCase,
    private val permission: PermissionProvider,
    private val resourceProvider: ResourceProvider,
    private val soundService: GetServiceUseCase
) : ViewModel(),
    VMDelegation<ActivityUIEffect, ActivityUIEvent, ActivityUIState> by
    VMDelegationImpl(ActivityUIState()) {

    private var stateBackgroundService = false
    private var stateForegroundService = false
    private var stateBoundService = false

    init {
        viewModel(this)
        event.collectIn(viewModelScope) { event ->
            when (event) {
                is ActivityUIEvent.ServiceStatusChanged -> {
                    serviceStatusChanged(event.name)
                }

                is ActivityUIEvent.BindToggleSound -> {
                    val text = if (event.isPlaying) resourceProvider.string(R.string.play_sound)
                    else resourceProvider.string(R.string.stop_sound)

                    setState(getCurrentState().copy(soundState = text))
                    setEffect(ActivityUIEffect.BoundToggleSound)
                }
            }
        }
        getServiceState()
    }

    private fun serviceStatusChanged(name: ServiceName) {
        when (name) {
            ServiceName.BACKGROUND -> {
                toggleService(name)

            }

            ServiceName.FOREGROUND -> {
                if (permission.checkReadStorage()) {
                    toggleService(name)
                } else launchPermission(name)
            }

            ServiceName.BOUND -> {
                if (permission.checkReadStorage()) {
                    toggleService(name)
                } else launchPermission(name)
            }
        }
    }

    private fun toggleService(name: ServiceName) {
        val effect = when (name) {
            ServiceName.BACKGROUND -> {
                setServiceStatus(!stateBackgroundService, name)
                if (!stateBackgroundService) ActivityUIEffect.StartService(SensorService::class.java)
                else ActivityUIEffect.StopService(SensorService::class.java)
            }

            ServiceName.FOREGROUND -> {
                if (!stateForegroundService)
                //Servisi BottomSheetFragment içerisinde başlatıyorum
                    ActivityUIEffect.ActionSelectMusic
                else
                    ActivityUIEffect.ShowError(resourceProvider.string(R.string.foreground_stop_error))

            }

            ServiceName.BOUND -> {
                setServiceStatus(!stateBoundService, name)
                if (!stateBoundService) ActivityUIEffect.StartService(SoundService::class.java)
                else ActivityUIEffect.StopService(SoundService::class.java)
            }
        }

        setEffect(effect)
    }

    private fun setServiceStatus(status: Boolean, name: ServiceName) = viewModelScope.launch {
        setServiceStatusUseCase.invoke(status, name)
    }

    private fun launchPermission(name: ServiceName) {
        val resultLaunch = when (name) {
            ServiceName.BACKGROUND -> {
                return
            }

            ServiceName.FOREGROUND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(
                        android.Manifest.permission.READ_MEDIA_AUDIO,

                        )
                } else {
                    listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }

            ServiceName.BOUND -> {
                //Cihazda bulunan şarkıyı başlattığım için izin istemem gerekiyor
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(
                        android.Manifest.permission.READ_MEDIA_AUDIO,

                        )
                } else {
                    listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
        setEffect(ActivityUIEffect.LaunchPermission(resultLaunch))
    }

    private fun getServiceState() {
        soundService[ServiceName.BACKGROUND].onEach {
            stateBackgroundService = it
            val serviceStateText = if (it) resourceProvider.string(R.string.stop_bg)
            else resourceProvider.string(R.string.start_bg)
            setState(
                getCurrentState().copy(
                    serviceState = serviceStateText,
                    serviceName = ServiceName.BACKGROUND
                )
            )
        }.launchIn(viewModelScope)

        soundService[ServiceName.BOUND].onEach {
            stateBoundService = it
            val serviceStateText = if (it) resourceProvider.string(R.string.stop_bound)
            else resourceProvider.string(R.string.start_bound)
            setState(
                getCurrentState().copy(
                    serviceState = serviceStateText,
                    serviceName = ServiceName.BOUND
                )
            )
        }.launchIn(viewModelScope)

        soundService[ServiceName.FOREGROUND].onEach {
            stateForegroundService = it
            val serviceStateText = if (it) resourceProvider.string(R.string.stop_fg)
            else resourceProvider.string(R.string.start_fg)
            setState(
                getCurrentState().copy(
                    serviceState = serviceStateText,
                    serviceName = ServiceName.FOREGROUND
                )
            )
        }.launchIn(viewModelScope)
    }
}
