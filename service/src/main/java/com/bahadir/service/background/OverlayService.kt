package com.bahadir.service.background

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import com.bahadir.service.R
import com.bahadir.service.databinding.OtherAppDesignBinding
import com.bahadir.service.watcher.ForegroundAppWatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {
    @Inject
    lateinit var foregroundAppWatcher: ForegroundAppWatcher
    private lateinit var windowManager: WindowManager
    private var binding: OtherAppDesignBinding? = null

    override fun onCreate() {
        super.onCreate()
        createOverlay()
        updateOverlayText(packageName)
        foregroundAppWatcher.startWatching {
            updateOverlayText(it)
        }
    }

    private fun createOverlay() {
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        binding = OtherAppDesignBinding.inflate(LayoutInflater.from(this))
        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            LayoutParams.TYPE_PHONE
        }
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LAYOUT_HEIGHT,
            layoutType,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.TOP or Gravity.CENTER
        windowManager.addView(binding?.root, layoutParams)

    }

    private fun updateOverlayText(packageName: String) {
        binding?.textAppName?.text = this.getString(R.string.package_name, packageName)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(binding?.root)
        binding = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        const val LAYOUT_HEIGHT = 200
    }
}