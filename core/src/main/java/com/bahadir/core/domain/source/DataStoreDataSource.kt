package com.bahadir.core.domain.source

import com.bahadir.core.common.ServiceName
import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {
    suspend fun setServiceStatus(status: Boolean, name: ServiceName)
    suspend fun getServiceStatus(name: ServiceName): Boolean
    fun getServiceStatusFlow(name: ServiceName): Flow<Boolean>
}