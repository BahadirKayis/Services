package com.bahadir.service.presentation.bound

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MessengerService : Service() {
    private lateinit var messenger: Messenger

    override fun onBind(intent: Intent?): IBinder? {
        messenger = Messenger(IncomingHandler(context = applicationContext))
        return messenger.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    //Handler sınıfı static olmalılıdır aksi taktirde sızıntılar meydana gelebilir
    internal class IncomingHandler(val context: Context) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG -> {
                    val replyMsg = msg.obj as String
                    Toast.makeText(context, replyMsg, Toast.LENGTH_SHORT).show()

                }
            }
            super.handleMessage(msg)
        }
    }

    companion object {
        const val MSG = 1
    }
}
