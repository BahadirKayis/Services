package com.bahadir.service.watcher

interface ForegroundAppWatcher {
    fun startWatching(packageName: (String) -> Unit)
    fun setPackageName(): String

}