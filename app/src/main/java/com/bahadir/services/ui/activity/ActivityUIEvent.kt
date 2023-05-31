package com.bahadir.services.ui.activity

import com.bahadir.core.base.Event
import com.bahadir.core.common.ServiceName

sealed class ActivityUIEvent : Event {
    data class ServiceStatusChanged(val name: ServiceName) : ActivityUIEvent()
    data class SendMessage(val text: String) : ActivityUIEvent()
    data class BindToggleSound(val isPlaying: Boolean) : ActivityUIEvent()
}