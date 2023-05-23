package com.bahadir.overlayservice.ui.selectmusic

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.ViewModelProvider
import com.bahadir.core.common.collectIn
import com.bahadir.core.data.model.IntentServiceMusicList
import com.bahadir.core.delegation.viewBinding
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.overlayservice.R
import com.bahadir.overlayservice.databinding.FragmentSelectMusicBinding
import com.bahadir.service.foreground.MusicPlayer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectMusicFragment : BottomSheetDialogFragment(R.layout.fragment_select_music) {
    private val binding by viewBinding(FragmentSelectMusicBinding::bind)
    private lateinit var viewModel: SelectMusicVM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SelectMusicVM::class.java]
        initUIState()
    }


    private fun initUIState() {
        val intent = Intent(requireContext(), MusicPlayer::class.java)
        viewModel.mutableFlow.collectIn(viewLifecycleOwner) { listMusic ->
            binding.rvMusic.adapter = MusicAdapter(listMusic) { positon ->
                intent.putExtra(SONG, IntentServiceMusicList(positon, listMusic))
                startForegroundService(requireContext(), intent)

            }
        }
    }

    private fun notificationControl(song: MusicUI) {
        val intent = Intent(requireContext(), MusicPlayer::class.java)
        val hasNotificationPermission = NotificationManagerCompat.from(requireContext())
            .areNotificationsEnabled()
        if (!hasNotificationPermission) {

        }

    }

    companion object {
        const val SONG = "song"
    }
}