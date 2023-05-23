package com.bahadir.service.provider

interface ForegroundAppWatcher {
    fun startWatching(packageName: (String) -> Unit)
    fun setPackageName(): String

}