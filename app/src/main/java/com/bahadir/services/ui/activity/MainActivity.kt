package com.bahadir.services.ui.activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bahadir.core.common.ServiceName
import com.bahadir.core.common.collectIn
import com.bahadir.core.common.showCustomSnackBar
import com.bahadir.core.delegation.viewBinding
import com.bahadir.service.presentation.bound.SoundService
import com.bahadir.services.databinding.ActivityMainBinding
import com.bahadir.services.ui.selectmusic.SelectMusicFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: ActivityVM by viewModels()
    private var soundService: SoundService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)

        initUIEffect()
        initUIEvent()
        initUIState()

    }


    private fun initUIEvent() {
        with(binding) {
            btnBackground.setOnClickListener {
                //setBroadcastReceiver()
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.BACKGROUND))
            }
            btnForeground.setOnClickListener {
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.FOREGROUND))
            }
            btnConvert.setOnClickListener {
                soundService?.let {
                    viewModel.setEvent(ActivityUIEvent.BindToggleSound(it.soundControl.isPlaying()))
                }
                // viewModel.setEvent(ActivityUIEvent.SendMessage(binding.etInput.text.toString()))
            }
            btnBound.setOnClickListener {
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.BOUND))
            }
        }
    }

    private fun initUIState() = viewModel.state.collectIn(this) { state ->
        when (state) {
            is ActivityUIState.ServiceState -> {
                buttonStateChange(state.name, state.serviceState)

            }

            is ActivityUIState.SoundState -> {
                binding.btnConvert.text = state.sound
            }

            else -> {}
        }
    }

    private fun buttonStateChange(name: ServiceName, text: String) {
        when (name) {
            ServiceName.BACKGROUND -> "$text BG".also {
                binding.btnBackground.text = it
            }

            ServiceName.FOREGROUND -> "$text FG".also {
                binding.btnForeground.text = it
            }

            ServiceName.BOUND -> "$text BD".also {
                binding.btnBound.text = it
            }
        }
    }

    private fun initUIEffect() = viewModel.effect.collectIn(this) { effect ->
        when (effect) {
            is ActivityUIEffect.StartService<*> -> {
                val intent = Intent(this, effect.intent)
                if (effect.intent == SoundService::class.java) {
                    bindService(
                        intent, serviceSound, BIND_AUTO_CREATE
                    )
                } else startService(intent)
            }

            is ActivityUIEffect.StopService<*> -> {
                if (effect.intent == SoundService::class.java) unbindService(serviceSound)
                else stopService(Intent(this, effect.intent))
            }

            is ActivityUIEffect.ShowError -> {
                binding.root.showCustomSnackBar(effect.message)
            }

            is ActivityUIEffect.ActionSelectMusic -> {
                SelectMusicFragment().show(supportFragmentManager, BOTTOM_SHEET_TAG)
            }

            is ActivityUIEffect.LaunchPermission -> {
                permissionForResult.launch(effect.permission.toTypedArray())
            }

            is ActivityUIEffect.BoundToggleSound -> {
                soundService?.soundControl?.toggleSound()
            }
        }
    }

    private val serviceSound = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SoundService.SoundBinder
            soundService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            soundService = null
        }
    }

    private val permissionForResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
//            if (isGranted.) {
//                viewModel.setEvent(ActivityUIEvent.ActionSelectMusic)
//            } else {
//                viewModel.setEffect(ActivityUIEffect.ShowError("Permission not granted"))
//            }
        }

    override fun onDestroy() {
        super.onDestroy()
        soundService?.let {
            viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.BOUND))
            unbindService(serviceSound)
        }


    }

    companion object {
        private const val BOTTOM_SHEET_TAG = "SelectMusicFragment"
    }
}