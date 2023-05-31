package com.bahadir.services.ui.activity

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahadir.core.common.ServiceName
import com.bahadir.core.common.collectIn
import com.bahadir.core.delegation.viewmodel.VMDelegation
import com.bahadir.core.delegation.viewmodel.VMDelegationImpl
import com.bahadir.core.domain.provider.PermissionProvider
import com.bahadir.core.domain.provider.ResourceProvider
import com.bahadir.core.domain.repository.ServicesRepository
import com.bahadir.core.domain.usecase.SetServiceUseCase
import com.bahadir.service.presentation.background.SensorService
import com.bahadir.service.presentation.bound.SoundService
import com.bahadir.services.R
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
    private val soundService: ServicesRepository
) : ViewModel(),
    VMDelegation<ActivityUIEffect, ActivityUIEvent, ActivityUIState> by VMDelegationImpl(
        ActivityUIState.LoadingState()
    ) {
    private var stateBackgroundService = false
    private var stateForegroundService = false
    private var statesBindService = false

    init {
        viewModel(this)
        event.collectIn(viewModelScope) { event ->
            when (event) {
                is ActivityUIEvent.ServiceStatusChanged -> {
                    serviceStatusChanged(event.name)
                }

                is ActivityUIEvent.SendMessage -> {
                    //sendMessage(event.text)
                }

                is ActivityUIEvent.BindToggleSound -> {
                    val text = if (event.isPlaying) resourceProvider.string(R.string.play_sound)
                    else resourceProvider.string(R.string.stop_sound)

                    setState(ActivityUIState.SoundState(text))
                    setEffect(ActivityUIEffect.BoundToggleSound)
                }
            }
        }
        serviceState()
    }

    private fun serviceStatusChanged(name: ServiceName) {
        when (name) {
            ServiceName.BACKGROUND -> {
                toggleService(name)
                stateBackgroundService = !stateBackgroundService
                setServiceStatus(stateBackgroundService, name)

            }

            ServiceName.FOREGROUND -> {
                if (permission.checkReadStorage()) {
                    toggleService(name)
                } else launchPermission(name)
            }

            ServiceName.BOUND -> {
                toggleService(name)
                statesBindService = !statesBindService
                setServiceStatus(statesBindService, name)

            }
        }
    }

    private fun toggleService(name: ServiceName) {
        val effect = when (name) {
            ServiceName.BACKGROUND -> {
                if (!stateBackgroundService) ActivityUIEffect.StartService(SensorService::class.java)
                else ActivityUIEffect.StopService(SensorService::class.java)
            }

            ServiceName.FOREGROUND -> {
                if (!stateForegroundService) ActivityUIEffect.ActionSelectMusic
                else ActivityUIEffect.ShowError(resourceProvider.string(R.string.foreground_stop_error))
            }

            ServiceName.BOUND -> {
                if (!statesBindService) ActivityUIEffect.StartService(SoundService::class.java)
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
                listOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }

            ServiceName.FOREGROUND -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    listOf(
                        android.Manifest.permission.READ_MEDIA_AUDIO,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )

                } else {
                    listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }

            ServiceName.BOUND -> {
                return
            }
        }
        setEffect(ActivityUIEffect.LaunchPermission(resultLaunch))

    }

    private fun serviceState() {
        soundService.getServiceStatusFlow(ServiceName.FOREGROUND).onEach {
            stateForegroundService = it
            if (it) setState(
                ActivityUIState.ServiceState(
                    resourceProvider.string(R.string.stop_service), ServiceName.FOREGROUND
                )
            )
            else setState(
                ActivityUIState.ServiceState(
                    resourceProvider.string(R.string.start_service), ServiceName.FOREGROUND
                )
            )
        }.launchIn(viewModelScope)
        soundService.getServiceStatusFlow(ServiceName.BACKGROUND).onEach {
            stateBackgroundService = it
            if (it) setState(
                ActivityUIState.ServiceState(
                    resourceProvider.string(R.string.stop_service), ServiceName.BACKGROUND
                )
            )
            else setState(
                ActivityUIState.ServiceState(
                    resourceProvider.string(R.string.start_service), ServiceName.BACKGROUND
                )
            )
        }.launchIn(viewModelScope)
        soundService.getServiceStatusFlow(ServiceName.BOUND).onEach {
            statesBindService = it
            if (it) setState(
                ActivityUIState.ServiceState(
                    resourceProvider.string(R.string.stop_service), ServiceName.BOUND
                )
            )
            else setState(
                ActivityUIState.ServiceState(
                    resourceProvider.string(R.string.start_service), ServiceName.BOUND
                )
            )
        }.launchIn(viewModelScope)
    }

//
//    //Messenger nesnesini kullanarak Activity ve Service arasında iletişim kurulur.
//    var messengerService: Messenger? = null
//
//    private fun sendMessage(text: String) {
//        messengerService?.let {
//            val msg = Message.obtain(null, MSG_BOUND_TEXT, 0, 0)
//            msg.obj = text
//            try {
//                it.send(msg)
//            } catch (e: RemoteException) {
//                e.printStackTrace()
//            }
//        }
//
//    }
//
//    // Bağlantı servis ile sağlandığında, bizimle iletişim kurmak için
//    // kullanabileceğimiz bir nesne elde ederiz. Servis ile bir Messenger
//    // aracılığıyla iletişim kuruyoruz, bu yüzden burada raw IBinder
//    // nesnesinden istemcinin tarafını temsil eden bir Messenger alırız.
//
//    val mConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            messengerService = Messenger(service)
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            messengerService = null
//        }
//    }
//
//
//    companion object {
//        const val MSG_BOUND_TEXT = 1
//    }
//
}
