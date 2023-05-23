package com.bahadir.service.provider

import android.app.Notification
import android.support.v4.media.session.MediaSessionCompat
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.service.enum.NotificationAction


interface NotificationControl {
    var mediaSession: MediaSessionCompat
    fun createNotificationChannel()
    fun showNotification(icon: NotificationAction, song: MusicUI): Notification
}