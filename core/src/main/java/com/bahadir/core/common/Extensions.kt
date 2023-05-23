package com.bahadir.core.common


import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.bahadir.core.R
import com.bahadir.core.databinding.CustomSnackbarBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar
import java.util.concurrent.TimeUnit

inline fun <reified T : Parcelable> Intent.parcelableList(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") (getParcelableExtra(key))
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Long.formatDuration(): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun Long.convertMinute(): Int {
    val startTime = (Calendar.getInstance().timeInMillis - this) / 1000
    val currentDate = Calendar.getInstance().timeInMillis / 1000
    val difference = (currentDate - startTime)

    return if (difference < 120) {
        1
    } else {
        (difference / 60).toInt()
    }

}

fun View.showCustomSnackBar(message: String) {
    val snackView = View.inflate(this.context, R.layout.custom_snackbar, null)
    val binding = CustomSnackbarBinding.bind(snackView)
    val snackBar = Snackbar.make(this, "", Snackbar.LENGTH_LONG)
    snackBar.apply {
        setBackgroundTint(resources.color(R.color.red_error_bg))
        (view as ViewGroup).addView(binding.root)
        binding.textMessage.text = message
        show()
    }
}

fun Resources.color(@ColorRes color: Int): Int {
    return ResourcesCompat.getColor(this, color, null)
}
