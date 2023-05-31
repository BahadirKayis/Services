package com.bahadir.core.domain.usecase

import com.bahadir.core.common.ServiceName
import com.bahadir.core.domain.repository.ServicesRepository
import javax.inject.Inject

class GetServiceUseCase @Inject constructor(private val servicesRepository: ServicesRepository) {
    suspend operator fun invoke(name: ServiceName) = servicesRepository.getServiceStatus(name)

}