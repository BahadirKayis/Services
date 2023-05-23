package com.bahadir.service.watcher

import android.app.usage.UsageStatsManager
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ForegroundAppWatcherImpl(
    context: Context,
    private val coroutineScope: CoroutineScope
) : ForegroundAppWatcher {
    private val usageStatsManager: UsageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    private var getPackageName = ""
    override fun startWatching(packageName: (String) -> Unit) {
        // büyük işlemler yapmadığım için  bu şekilde kullanmam pek bir sorun teşkil etmez :)
        //scope da büyük işlemler yapacak olsaydım bellek ve pil tasarrufu sağlamak için alabileceğim en basit önlem
        //BroadcastReceiver ile SCREEN_ON ve SCREEN_OFF actionlarını yakalayıp ona göre davranmak olurdu
        coroutineScope.launch {
            while (true) {
                getPackageName = setPackageName()
                if (getPackageName.isNotEmpty()) {
                    packageName(getPackageName)
                }
                delay(DELAY)
            }
            //Hilt coroutineScope nesnemizi yönettği için servis durdurulduğu zaman cancel etmeme gerek yok
        }
    }


    override fun setPackageName(): String {
        val time = System.currentTimeMillis()
        val stats =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time - DELAY, time)
        return if (stats != null) {
            stats.maxByOrNull { it.lastTimeUsed }?.packageName ?: ""
        } else {
            ""
        }
    }

    companion object {
        const val DELAY = 1000L
    }
}