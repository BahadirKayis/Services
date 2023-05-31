package com.bahadir.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.bahadir.core.common.ServiceName
import com.bahadir.core.domain.usecase.GetServiceUseCase
import com.bahadir.service.presentation.background.SensorService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class BootBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repo: GetServiceUseCase

    @Inject
    @Named("IO")
    lateinit var coroutineScope: CoroutineScope
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_BOOT_COMPLETED -> {
                coroutineScope.launch {
                    if (repo.invoke(ServiceName.BACKGROUND)) {
                        val serviceIntent = Intent(context, SensorService::class.java)
                        context?.startService(serviceIntent)
                    }
                }
            }
        }
    }

}
