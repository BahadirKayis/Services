package com.bahadir.overlayservice.ui.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bahadir.core.common.collectIn
import com.bahadir.core.common.showCustomSnackBar
import com.bahadir.core.common.visible
import com.bahadir.core.delegation.viewBinding
import com.bahadir.core.domain.provider.PermissionProvider
import com.bahadir.overlayservice.R
import com.bahadir.overlayservice.databinding.ActivityMainBinding
import com.bahadir.overlayservice.ui.selectmusic.SelectMusicFragment
import com.bahadir.service.background.OverlayService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: ActivityVM by viewModels()
    private var serviceStatus: Boolean = false

    @Inject
    lateinit var permission: PermissionProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        initUIEffect()
        initUIEvent()
        initUIState()
        createNotificationChannel()
    }

    private fun backToApp() = lifecycleScope.launch {
        while (true) {
            if (permission.checkUsageStats()) {
                val intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            delay(DELAY)
        }
    }

    private fun initUIEvent() {
        with(binding) {
            btnBackground.setOnClickListener {
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(!serviceStatus))
            }
            btnForeground.setOnClickListener {
                viewModel.setEvent(ActivityUIEvent.ActionSelectMusic)
            }
        }

    }

    private fun initUIState() = viewModel.state.collectIn(this) { state ->
        when (state) {
            is ActivityUIState.ServiceStatus -> {
                if (state.serviceStatus) binding.btnBackground.text =
                    getString(R.string.stop_service)
                else binding.btnBackground.text = getString(R.string.start_background_service)

                serviceStatus = state.serviceStatus
            }

            is ActivityUIState.AppUsageTime -> {
                binding.rcAppTime.visible()
                binding.rcAppTime.adapter = AppsUsageTimeAdapter(state.appUsageTime)
            }

            else -> {}
        }
    }

    private fun initUIEffect() = viewModel.effect.collectIn(this) { effect ->
        val overlayService = Intent(this, OverlayService::class.java)
        when (effect) {
            is ActivityUIEffect.ActionDrawOtherApp -> {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(intent)
            }

            is ActivityUIEffect.ActionUsageAccessSettings -> {
                backToApp()
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivity(intent)
            }

            is ActivityUIEffect.StartOverlayService -> {
                startService(overlayService)
            }

            is ActivityUIEffect.StopOverlayService -> {
                stopService(overlayService)
            }

            is ActivityUIEffect.ShowError -> {
                binding.root.showCustomSnackBar(effect.message)
            }

            is ActivityUIEffect.BackActivity -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            is ActivityUIEffect.ActionSelectMusic -> {
                SelectMusicFragment().show(supportFragmentManager, BOTTOM_SHEET_TAG)
            }

            is ActivityUIEffect.ActionReadStoragePermission -> {
//                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    android.Manifest.permission.READ_MEDIA_AUDIO
//
//                } else {
//
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE
//
//                }
//
//                permissionForResult.launch(permission)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionForResult.launch(
                        arrayOf(
                            android.Manifest.permission.READ_MEDIA_AUDIO,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        )
                    )
                }
            }
        }
    }

    private val permissionForResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
//            if (isGranted) {
//                viewModel.setEvent(ActivityUIEvent.ActionSelectMusic)
//            } else {
//                viewModel.setEffect(ActivityUIEffect.ShowError("Permission not granted"))
//            }
        }


    companion object {
        private const val DELAY = 1000L
        private const val BOTTOM_SHEET_TAG = "SelectMusicFragment"
        private const val CHANNEL_ID = "channel1"
        private const val CHANNEL_NAME = "Foreground Service Channel"
    }


    //Foreground Service
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "This is foreground service channel"
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChannel)
        }

    }
}