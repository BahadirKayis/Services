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
import com.bahadir.service.presentation.bound.SoundService
import com.bahadir.services.databinding.ActivityMainBinding
import com.bahadir.services.delegation.viewBinding
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
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.BACKGROUND))
            }
            btnForeground.setOnClickListener {
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.FOREGROUND))
            }
            btnSound.setOnClickListener {
                soundService?.let {
                    viewModel.setEvent(ActivityUIEvent.BindToggleSound(it.soundControl.isPlaying()))
                }
            }
            btnBound.setOnClickListener {
                viewModel.setEvent(ActivityUIEvent.ServiceStatusChanged(ServiceName.BOUND))
            }
        }
    }

    private fun initUIState() = viewModel.state.collectIn(this) { state ->
        state.serviceName?.let {
            buttonStateChange(it, state.serviceState!!)
        }
        state.soundState?.let {
            binding.btnSound.text = it
        }
    }

    private fun buttonStateChange(name: ServiceName, text: String) {
        when (name) {
            ServiceName.BACKGROUND -> binding.btnBackground.text = text

            ServiceName.FOREGROUND -> binding.btnForeground.text = text

            ServiceName.BOUND -> binding.btnBound.text = text
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
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            // İzinlerin sonuçları alındığında yapılacak işlemler
            // permissions parametresi, izinlerin durumunu içeren bir Map  nesnesidir.

            // İzinlerin durumunu kontrol etmek için
//            if (isGranted[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
//                // İzin verildiyse yapılacak işlemler
//
//            } else {
//                // İzin reddedildiyse yapılacak işlemler
//
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