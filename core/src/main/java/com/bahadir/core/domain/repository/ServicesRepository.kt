package com.bahadir.core.domain.repository

import com.bahadir.core.common.Resource
import com.bahadir.core.common.ServiceName
import com.bahadir.core.data.model.MusicUI
import kotlinx.coroutines.flow.Flow

interface ServicesRepository {
    suspend fun setServiceStatus(status: Boolean, name: ServiceName)
    suspend fun getServiceStatus(name: ServiceName): Boolean
    fun getServiceStatusFlow(name: ServiceName): Flow<Boolean>
    fun getMusicList(): Flow<Resource<List<MusicUI>>>
}