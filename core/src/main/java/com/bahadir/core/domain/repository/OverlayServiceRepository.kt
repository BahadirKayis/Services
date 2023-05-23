package com.bahadir.core.domain.repository

import com.bahadir.core.common.Resource
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.core.domain.model.UsageStateUI
import kotlinx.coroutines.flow.Flow

interface OverlayServiceRepository {
    suspend fun setServiceStatus(status: Boolean)
    suspend fun getServiceStatus(): Boolean
    suspend fun setServiceStartTime(startTime: Long)
    fun getUsageStatesTime(): Flow<Resource<List<UsageStateUI>>>
    fun getMusicList(): Flow<Resource<List<MusicUI>>>
}