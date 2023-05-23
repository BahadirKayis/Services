package com.bahadir.core.domain.usecase

import com.bahadir.core.domain.repository.OverlayServiceRepository
import javax.inject.Inject

class SetStartTimeUseCase @Inject constructor(private val repo: OverlayServiceRepository) {
    suspend operator fun invoke(time: Long) = repo.setServiceStartTime(time)

}