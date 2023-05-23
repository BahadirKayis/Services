package com.bahadir.service.foreground

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bahadir.core.common.parcelableList
import com.bahadir.core.data.model.IntentServiceMusicList
import com.bahadir.core.domain.provider.MusicControl
import com.bahadir.service.enum.NotificationAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MusicPlayer : Service() {
    @Inject
    lateinit var musicControl: MusicControl
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        musicControl.stopNotification()
        stopSelf()
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
                    musicControl.changeSong(-1)
                }

                NotificationAction.EXIT -> {
                    musicControl.stopNotification()
                    stopSelf()
                }

                else -> {
                    musicControl.musicStateUpdate(value)
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun startUpdateForeground(notification: Notification) {
        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        const val ARG_PARAM1 = "song"
        const val NOTIFICATION_ID = 1
    }
}