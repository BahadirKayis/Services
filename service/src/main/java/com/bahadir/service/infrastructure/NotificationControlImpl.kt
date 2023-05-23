package com.bahadir.service.infrastructure

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.service.R
import com.bahadir.service.enum.NotificationAction
import com.bahadir.service.foreground.MusicPlayer
import com.bahadir.service.provider.NotificationControl
import javax.inject.Inject

class NotificationControlImpl @Inject constructor(val context: Context, val service: Service) :
    NotificationControl {

    override lateinit var mediaSession: MediaSessionCompat

    override fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "This is foreground service channel"
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)
        }
    }

    override fun showNotification(icon: NotificationAction, song: MusicUI): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(song.title)
            .setContentText(song.artist)
            .setSmallIcon(R.drawable.ic_music)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.music))
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).addAction(
                R.drawable.ic_skip_previous,
                NotificationAction.PREVIOUS.name,
                pendingIntent(action = NotificationAction.PREVIOUS)
            ).addAction(
                icon.invoke(),
                icon.name,
                pendingIntent(action = icon)
            )
            .addAction(
                R.drawable.ic_skip_next,
                NotificationAction.NEXT.name,
                pendingIntent(action = NotificationAction.NEXT)
            )
            .addAction(
                R.drawable.ic_close,
                NotificationAction.EXIT.name,
                pendingIntent(action = NotificationAction.EXIT)
            )
            .setOnlyAlertOnce(true)
            .setOngoing(false)
            .build()


    }

    private fun pendingIntent(action: NotificationAction): PendingIntent {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val intent = Intent(context, MusicPlayer::class.java).setAction(action.name)
        return PendingIntent.getService(context, 0, intent, flag)
    }

    companion object {
        private const val CHANNEL_ID = "channel1"
        private const val CHANNEL_NAME = "Foreground Service Channel"
    }
}
