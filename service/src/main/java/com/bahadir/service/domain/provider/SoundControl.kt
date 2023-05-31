package com.bahadir.service.domain.provider

import android.net.Uri

interface SoundControl {
    fun soundCreate(sound: Uri = Uri.parse("content://media/external/audio/media/1000000033"))
    fun playSound()
    fun stopSound()
    fun isPlaying(): Boolean
    fun toggleSound()
}