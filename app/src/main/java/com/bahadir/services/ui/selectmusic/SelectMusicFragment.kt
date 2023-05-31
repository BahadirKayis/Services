package com.bahadir.services.ui.selectmusic

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.ViewModelProvider
import com.bahadir.core.common.collectIn
import com.bahadir.core.data.model.IntentServiceMusicList
import com.bahadir.core.delegation.viewBinding
import com.bahadir.service.presentation.foreground.MusicPlayerService
import com.bahadir.services.R
import com.bahadir.services.databinding.FragmentSelectMusicBinding
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
        val intent = Intent(requireContext(), MusicPlayerService::class.java)
        viewModel.songList.collectIn(viewLifecycleOwner) { listMusic ->
            binding.rvMusic.adapter = MusicAdapter(listMusic) { position ->
                intent.putExtra(SONG, IntentServiceMusicList(position, listMusic))
                startForegroundService(requireContext(), intent)
                viewModel.setServiceStatues()

            }
        }
    }

    companion object {
        const val SONG = "song"
    }
}