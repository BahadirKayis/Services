package com.bahadir.service.domain.provider

import android.app.Notification
import com.bahadir.core.data.model.MusicUI
import com.bahadir.service.common.NotificationAction


internal interface MusicControl {
    var songList: List<MusicUI>
    var songPosition: Int
    fun startSong(): Notification
    fun changeSong(changeSong: Int): Notification
    fun musicStateUpdate(state: NotificationAction): Notification
    fun stopNotification()
}