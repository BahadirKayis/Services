package com.bahadir.services.ui.selectmusic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahadir.core.common.Resource
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.core.domain.usecase.GetMusicListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SelectMusicVM @Inject constructor(private val getMusic: GetMusicListUseCase) : ViewModel() {
    val mutableFlow: MutableStateFlow<List<MusicUI>> = MutableStateFlow(listOf())

    init {
        getMusic().onEach {
            when (it) {
                is Resource.Success -> {
                    mutableFlow.emit(it.data)
                }

                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)

    }

}