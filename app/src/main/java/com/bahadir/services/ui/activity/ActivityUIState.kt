package com.bahadir.services.ui.activity

import com.bahadir.services.base.State
import com.bahadir.core.common.ServiceName

sealed class ActivityUIState : State {
    data class LoadingState(var isLoading: Boolean = false) : ActivityUIState()
    data class ServiceState(val serviceState: String, val name: ServiceName) : ActivityUIState()
    data class SoundState(val sound: String) : ActivityUIState()
    data class ConvertedText(val text: String) : ActivityUIState()
}