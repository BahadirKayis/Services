package com.bahadir.service.infrastructure

import android.app.Notification
import android.content.Context
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import com.bahadir.core.data.model.MusicUI
import com.bahadir.service.common.NotificationAction
import com.bahadir.service.domain.provider.MusicControl
import com.bahadir.service.domain.provider.NotificationControl


internal class MusicControlImpl(
    private val context: Context,
    private val notification: NotificationControl,
) : MusicControl {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    override lateinit var songList: List<MusicUI>
    override var songPosition: Int = 0

    init {
        notification.createNotificationChannel()
        notification.mediaSession = MediaSessionCompat(context, TAG_MEDIA_SESSION)

    }

    override fun startSong(): Notification {
        mediaPlayer.setDataSource(context, songList[songPosition].contentUri)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            changeSong(1)
        }
        return notification.showNotification(NotificationAction.PLAY, songList[songPosition])
    }

    override fun changeSong(changeSong: Int): Notification {
        songPosition += changeSong
        if (songPosition > songList.size - 1) {
            songPosition = 0
        } else if (songPosition < 0) {
            songPosition = songList.size - 1
        }
        mediaPlayer.reset()
        return startSong()
    }

    override fun musicStateUpdate(state: NotificationAction): Notification {
        val newState = when (state) {
            NotificationAction.PLAY -> {
                mediaPlayer.pause()
                NotificationAction.PAUSE
            }

            NotificationAction.PAUSE -> {
                mediaPlayer.start()
                NotificationAction.PLAY
            }

            else -> {
                NotificationAction.PLAY
            }
        }
        return notification.showNotification(newState, songList[songPosition])
    }


    override fun stopNotification() {
        mediaPlayer.stop()
    }

    companion object {
        const val TAG_MEDIA_SESSION = "MediaSessionTag"
    }
}

