package com.bahadir.core.domain.provider

interface PermissionProvider {
    fun checkDrawOverlay(): Boolean
    fun checkUsageStats(): Boolean
    fun checkReadStorage(): Boolean

}