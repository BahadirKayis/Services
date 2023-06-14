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
    lateinit var setServiceStatus: SetServiceUseCase

    @Inject
    @Named("IO")
    lateinit var coroutineScope: CoroutineScope
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.parcelableList<IntentServiceMusicList>(MUSIC)?.let { music ->
            musicControl.songList = music.musicList
            musicControl.songPosition = music.position
            startOrUpdateForeground(musicControl.startSong())

        }
        intent?.action?.let { action ->
            when (val value = NotificationAction.valueOf(action)) {
                NotificationAction.NEXT -> {
                    startOrUpdateForeground(musicControl.changeSong(1))
                }

                NotificationAction.PREVIOUS -> {

                    startOrUpdateForeground(musicControl.changeSong(-1))
                }

                NotificationAction.EXIT -> {
                    musicControl.stopNotification()
                    stopSelf()
                }

                else -> {
                    startOrUpdateForeground(musicControl.musicStateUpdate(value))
                }
            }
        }
        return START_STICKY
    }

    private fun startOrUpdateForeground(notification: Notification) {
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        musicControl.stopNotification()
        coroutineScope.launch {
            setServiceStatus(false, ServiceName.FOREGROUND)
        }

    }

    companion object {
        const val MUSIC = "music"
        const val NOTIFICATION_ID = 1
    }
}