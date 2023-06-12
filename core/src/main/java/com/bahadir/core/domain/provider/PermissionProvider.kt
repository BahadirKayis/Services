package com.bahadir.core.domain.provider

interface PermissionProvider {
    fun checkReadStorage(): Boolean

}