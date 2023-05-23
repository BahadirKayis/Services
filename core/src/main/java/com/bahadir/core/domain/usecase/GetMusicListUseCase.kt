package com.bahadir.core.domain.usecase

import com.bahadir.core.domain.repository.OverlayServiceRepository
import javax.inject.Inject

class GetMusicListUseCase @Inject constructor(val repository: OverlayServiceRepository) {

    operator fun invoke() = repository.getMusicList()
}