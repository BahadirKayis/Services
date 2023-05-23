package com.bahadir.core.domain.mapper

import android.app.usage.UsageStats
import com.bahadir.core.common.convertMinute
import com.bahadir.core.domain.model.UsageStateUI

fun List<UsageStats>.mapToUsageStateUI() = map {
    UsageStateUI(
        appPackageName = it.packageName,
        usageTime = it.totalTimeInForeground.convertMinute(),
        appName = null,
        icon = null
    )
}.toMutableList()