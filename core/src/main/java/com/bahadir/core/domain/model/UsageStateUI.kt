package com.bahadir.core.domain.model

import android.graphics.drawable.Drawable

data class UsageStateUI(
    val appPackageName: String,
    val usageTime: Int,
    var icon: Drawable? = null,
    var appName: String? = null
)
