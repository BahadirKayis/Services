package com.bahadir.core.domain.usecase

import com.bahadir.core.domain.repository.OverlayServiceRepository
import javax.inject.Inject

class GetServiceUseCase @Inject constructor(private val overlayServiceRepository: OverlayServiceRepository) {
    suspend operator fun invoke() = overlayServiceRepository.getServiceStatus()
}