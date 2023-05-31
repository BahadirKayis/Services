package com.bahadir.services.ui.selectmusic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahadir.core.common.Resource
import com.bahadir.core.common.ServiceName
import com.bahadir.core.data.model.MusicUI
import com.bahadir.core.domain.usecase.GetMusicListUseCase
import com.bahadir.core.domain.usecase.SetServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectMusicVM @Inject constructor(
    getMusic: GetMusicListUseCase,
    private val setGetService: SetServiceUseCase,
) : ViewModel() {
    val songList: MutableStateFlow<List<MusicUI>> = MutableStateFlow(listOf())

    init {
        getMusic().onEach {
            when (it) {
                is Resource.Success -> {
                    songList.emit(it.data)
                }

                is Resource.Error -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun setServiceStatues() = viewModelScope.launch {
        setGetService.invoke(true, ServiceName.FOREGROUND)
    }
}