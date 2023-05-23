package com.bahadir.core.domain.source

interface DataStoreDataSource {
    suspend fun getServiceStatus(): Boolean
    suspend fun setServiceStatus(status: Boolean)
    suspend fun getServiceStartTime(): Long
    suspend fun setServiceStartTime(startTime: Long)
}