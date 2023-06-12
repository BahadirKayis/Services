package com.bahadir.core.infrastructure


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.bahadir.core.domain.provider.PermissionProvider

class PermissionProviderImpl(private val context: Context) : PermissionProvider {
    override fun checkReadStorage(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE, context.packageName
            )
        }
    }
}