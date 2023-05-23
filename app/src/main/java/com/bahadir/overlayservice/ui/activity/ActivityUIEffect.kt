package com.bahadir.overlayservice.ui.activity

import com.bahadir.core.base.Effect

sealed class ActivityUIEffect : Effect {
    object ActionDrawOtherApp : ActivityUIEffect()
    object ActionUsageAccessSettings : ActivityUIEffect()
    object StartOverlayService : ActivityUIEffect()
    object StopOverlayService : ActivityUIEffect()
    object BackActivity : ActivityUIEffect()
    object ActionSelectMusic : ActivityUIEffect()
    object ActionReadStoragePermission : ActivityUIEffect()
    data class ShowError(val message: String) : ActivityUIEffect()

}