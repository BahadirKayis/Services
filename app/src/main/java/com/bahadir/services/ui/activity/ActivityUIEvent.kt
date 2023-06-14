package com.bahadir.services.ui.activity

import com.bahadir.services.base.Event
import com.bahadir.core.common.ServiceName

sealed class ActivityUIEvent : Event {
    data class ServiceStatusChanged(val name: ServiceName) : ActivityUIEvent()
    data class BindToggleSound(val isPlaying: Boolean) : ActivityUIEvent()
}