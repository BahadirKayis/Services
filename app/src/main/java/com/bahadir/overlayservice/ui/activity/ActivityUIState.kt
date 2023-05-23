package com.bahadir.overlayservice.ui.activity

import com.bahadir.core.base.State
import com.bahadir.core.domain.model.UsageStateUI

sealed class ActivityUIState : State {
    data class LoadingState(var isLoading: Boolean = false) : ActivityUIState()
    data class ServiceStatus(val serviceStatus: Boolean) : ActivityUIState()
    data class AppUsageTime(val appUsageTime: List<UsageStateUI>) : ActivityUIState()

}