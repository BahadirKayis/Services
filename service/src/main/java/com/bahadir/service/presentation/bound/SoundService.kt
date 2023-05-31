package com.bahadir.service.presentation.bound

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.bahadir.service.domain.provider.SoundControl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SoundService : Service() {
    @Inject
    lateinit var soundControl: SoundControl


    override fun onBind(intent: Intent?): IBinder {
        soundControl.soundCreate()
        return SoundBinder()
    }

    inner class SoundBinder : Binder() {
        fun getService(): SoundService = this@SoundService
    }
}