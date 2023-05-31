package com.bahadir.service.domain.provider

import android.app.Notification
import android.support.v4.media.session.MediaSessionCompat
import com.bahadir.core.data.model.MusicUI
import com.bahadir.service.common.NotificationAction


internal interface NotificationControl {
    var mediaSession: MediaSessionCompat
    fun createNotificationChannel()
    fun showNotification(icon: NotificationAction, song: MusicUI): Notification
}