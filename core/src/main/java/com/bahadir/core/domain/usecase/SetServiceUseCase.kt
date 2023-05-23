package com.bahadir.core.domain.usecase

import com.bahadir.core.domain.repository.OverlayServiceRepository
import javax.inject.Inject

class SetServiceUseCase @Inject constructor(private val repo: OverlayServiceRepository) {
    suspend operator fun invoke(status: Boolean) = repo.setServiceStatus(status)

}