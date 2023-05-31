package com.bahadir.core.domain.usecase

import com.bahadir.core.common.ServiceName
import com.bahadir.core.domain.repository.ServicesRepository
import javax.inject.Inject

class SetServiceUseCase @Inject constructor(private val repo: ServicesRepository) {
    suspend operator fun invoke(status: Boolean, name: ServiceName) =
        repo.setServiceStatus(status, name)

}