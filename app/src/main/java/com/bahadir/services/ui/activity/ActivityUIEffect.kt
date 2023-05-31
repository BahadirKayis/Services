package com.bahadir.services.ui.activity

import com.bahadir.core.base.Effect

sealed class ActivityUIEffect : Effect {
    data class StartService<T>(val intent: Class<T>) : ActivityUIEffect()
    data class StopService<T>(val intent: Class<T>) : ActivityUIEffect()
    object ActionSelectMusic : ActivityUIEffect()
    object BoundToggleSound : ActivityUIEffect()
    data class LaunchPermission(val permission: List<String>) : ActivityUIEffect()
    data class ShowError(val message: String) : ActivityUIEffect()

}