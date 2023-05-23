package com.bahadir.core.data.source

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.bahadir.core.domain.mapper.mapToUsageStateUI
import com.bahadir.core.domain.model.UsageStateUI
import com.bahadir.core.domain.source.UsageStateDataSource
import java.util.Calendar

class UsageStateDataSourceImpl(private val context: Context) : UsageStateDataSource {
    override fun getUsageStatesTime(startTime: Long): List<UsageStateUI> {
        val usageStatsManager: UsageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        //Her ne kadar yavaş ve pil tüketimi yüksek olsa da
        // Doğruluk payı önemli olduğu için INTERVAL_BEST kullanıyorum

        val calendarEnd = Calendar.getInstance().timeInMillis
        val data = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST, startTime, calendarEnd
        )

        val usageState = data.filter { usageStats ->
            usageStats.totalTimeInForeground > 0 && usageStats.lastTimeStamp in startTime..calendarEnd
        }.mapToUsageStateUI()

        return getInstalledApps(usageState)
    }


    private fun getInstalledApps(usageState: MutableList<UsageStateUI>): List<UsageStateUI> {
        val packs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getInstalledPackages(
                PackageManager.PackageInfoFlags.of(PackageManager.GET_META_DATA.toLong())
            )
        } else {
            context.packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA
            )
        }
        packs.forEach { p ->
            val appName =
                p.applicationInfo.loadLabel(context.packageManager).toString()
            val icon = p.applicationInfo.loadIcon(context.packageManager)
            val packages = p.applicationInfo.packageName

            usageState.find { it.appPackageName == packages }?.let {
                it.appName = appName
                it.icon = icon
            }
        }

        return usageState.sortedByDescending { it.usageTime }
    }


}