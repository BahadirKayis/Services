package com.bahadir.core.common


import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Parcelable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

inline fun <reified T : Parcelable> Intent.parcelableList(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") (getParcelableExtra(key))
}

fun Long.formatDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format(format = "%02d:%02d", minutes, seconds)
}

fun View.showCustomSnackBar(message: String) {
    Snackbar.make(this, message, 2500).show()
}

fun Resources.color(@ColorRes color: Int): Int {
    return ResourcesCompat.getColor(this, color, null)
}
