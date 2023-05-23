package com.bahadir.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bahadir.core.domain.repository.OverlayServiceRepository
import com.bahadir.service.background.OverlayService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repo: OverlayServiceRepository

    @Inject
    @Named("IO")
    lateinit var coroutineScope: CoroutineScope
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                coroutineScope.launch {
                    if (repo.getServiceStatus()) {
                        val serviceIntent = Intent(context, OverlayService::class.java)
                        context?.startService(serviceIntent)
                    }
                }
            }
        }
    }

}
