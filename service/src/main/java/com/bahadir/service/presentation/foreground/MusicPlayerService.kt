package com.bahadir.service.presentation.foreground

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bahadir.core.common.ServiceName
import com.bahadir.core.common.parcelableList
import com.bahadir.core.data.model.IntentServiceMusicList
import com.bahadir.core.domain.usecase.SetServiceUseCase
import com.bahadir.service.common.NotificationAction
import com.bahadir.service.domain.provider.MusicControl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class MusicPlayerService : Service() {
    @Inject
    internal lateinit var musicControl: MusicControl

    @Inject
    lateinit var setServiceUseCase: SetServiceUseCase

    @Inject
    @Named("IO")
    lateinit var coroutine: CoroutineScope


    override fun onDestroy() {
        super.onDestroy()
        coroutine.launch {
            setServiceUseCase(false, ServiceName.FOREGROUND)
        }
        musicControl.stopNotification()
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.parcelableList<IntentServiceMusicList>(ARG_PARAM1)?.let { music ->
            musicControl.songList = music.musicList
            musicControl.songPosition = music.position
            startUpdateForeground(musicControl.startSong())

        }
        intent?.action?.let { action ->
            when (val value = NotificationAction.valueOf(action)) {
                NotificationAction.NEXT -> {
                    startUpdateForeground(musicControl.changeSong(1))
                }

                NotificationAction.PREVIOUS -> {

                    startUpdateForeground(musicControl.changeSong(-1))
                }

                NotificationAction.EXIT -> {
                    musicControl.stopNotification()
                    stopSelf()
                }

                else -> {
                    startUpdateForeground(musicControl.musicStateUpdate(value))
                }
            }
        }
        return START_STICKY
    }

    private fun startUpdateForeground(notification: Notification) {
        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        const val ARG_PARAM1 = "song"
        const val NOTIFICATION_ID = 1
    }
}