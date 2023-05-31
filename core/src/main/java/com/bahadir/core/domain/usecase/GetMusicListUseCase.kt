package com.bahadir.core.domain.usecase

import com.bahadir.core.domain.repository.ServicesRepository
import javax.inject.Inject

class GetMusicListUseCase @Inject constructor(val repository: ServicesRepository) {
    operator fun invoke() = repository.getMusicList()
}