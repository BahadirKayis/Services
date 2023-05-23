package com.bahadir.services.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahadir.core.common.Resource
import com.bahadir.core.common.collectIn
import com.bahadir.core.delegation.viewmodel.VMDelegation
import com.bahadir.core.delegation.viewmodel.VMDelegationImpl
import com.bahadir.core.domain.provider.PermissionProvider
import com.bahadir.core.domain.usecase.GetServiceUseCase
import com.bahadir.core.domain.usecase.GetUsageStateUseCase
import com.bahadir.core.domain.usecase.SetServiceUseCase
import com.bahadir.core.domain.usecase.SetStartTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ActivityVM @Inject constructor(
    private val getServiceStatusUseCase: GetServiceUseCase,
    private val setServiceStatusUseCase: SetServiceUseCase,
    private val setStartTimeUseCase: SetStartTimeUseCase,
    private val permission: PermissionProvider,
    private val getUsageTimeUseCase: GetUsageStateUseCase,
) : ViewModel(),
    VMDelegation<ActivityUIEffect, ActivityUIEvent, ActivityUIState> by VMDelegationImpl(
        ActivityUIState.LoadingState()
    ) {
    init {
        viewModel(this)
        event.collectIn(viewModelScope) { event ->
            when (event) {
                is ActivityUIEvent.ServiceStatusChanged -> {
                    if (event.status) checkPermission()
                    else {
                        setEffect(ActivityUIEffect.StopOverlayService)
                        setServiceStatus(false)
                        getUsageState()
                    }
                }

                is ActivityUIEvent.ActionSelectMusic -> {
                    checkMusicSelectionPermission()
                }
            }
        }
        getServiceStatus()
    }

    private fun checkPermission() {
        if (permission.checkUsageStats()) {
            if (permission.checkDrawOverlay()) {
                setEffect(ActivityUIEffect.StartOverlayService)
                setServiceStatus(true)
                setStartTime()
            } else {
                setEffect(ActivityUIEffect.ActionDrawOtherApp)
            }
        } else {
            setEffect(ActivityUIEffect.ActionUsageAccessSettings)
        }
    }

    private fun setServiceStatus(status: Boolean) = viewModelScope.launch {
        setServiceStatusUseCase.invoke(status)
        getServiceStatus()
    }

    private fun getServiceStatus() = viewModelScope.launch {
        val result = getServiceStatusUseCase.invoke()
        setState(ActivityUIState.ServiceStatus(result))
    }

    private fun getUsageState() {
        getUsageTimeUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    setState(ActivityUIState.AppUsageTime(result.data))
                }

                is Resource.Error -> {
                    setEffect(ActivityUIEffect.ShowError(result.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun setStartTime() = viewModelScope.launch {
        val startTime = Calendar.getInstance().timeInMillis
        setStartTimeUseCase(startTime)
    }

    private fun checkMusicSelectionPermission() {
        if (permission.checkReadStorage())
            setEffect(ActivityUIEffect.ActionSelectMusic)
        else
            setEffect(ActivityUIEffect.ActionReadStoragePermission)
    }
}
