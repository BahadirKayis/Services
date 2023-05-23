package com.bahadir.core.domain.provider

import android.app.Notification
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.service.enum.NotificationAction

interface MusicControl {
    var songList: List<MusicUI>
    var songPosition: Int
    fun startSong(): Notification
    fun changeSong(changeSong: Int): Notification
    fun musicStateUpdate(state: NotificationAction)
    fun audioFocusChange()
    fun stopNotification()
}