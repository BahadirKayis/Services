package com.bahadir.service.infrastructure

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.bahadir.service.domain.provider.SoundControl

class SoundControlImpl(val context: Context) : SoundControl {
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    override fun soundCreate(sound: Uri) {
        mediaPlayer.setDataSource(context, sound)
        mediaPlayer.prepare()
    }


    override fun playSound() {
        mediaPlayer.start()
    }

    override fun stopSound() {
        mediaPlayer.stop()
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

    override fun toggleSound() {
        if (isPlaying()) stopSound()
        else playSound()
    }

}
