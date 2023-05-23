package com.bahadir.core.infrastructure


import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import com.bahadir.core.domain.provider.PermissionProvider

class PermissionProviderImpl(private val context: Context) : PermissionProvider {
    override fun checkDrawOverlay(): Boolean {
        //Diğer uygulamaların üzerine çizim yapabilmek için  izin kontrol
        return Settings.canDrawOverlays(context)
    }

    override fun checkUsageStats(): Boolean {
        //Diğer uygulamaların kullanım istatistiklerini alabilmek izin kontrol
        val appOpsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun checkReadStorage(): Boolean {
        //Diğer uygulamaların depolama alanına erişebilmek için izin kontrol
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            PackageManager.PERMISSION_GRANTED == context.packageManager.checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                context.packageName
            )
        }
    }
}