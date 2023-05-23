package com.bahadir.overlayservice.ui.activity

import com.bahadir.core.base.Event

sealed class ActivityUIEvent : Event {
    data class ServiceStatusChanged(val status: Boolean) : ActivityUIEvent()
    object ActionSelectMusic : ActivityUIEvent()
}