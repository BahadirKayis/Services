package com.bahadir.service.common

import com.bahadir.service.R


internal enum class NotificationAction {
    NEXT, PREVIOUS, PLAY, EXIT, PAUSE;

    operator fun invoke(): Int {
        return when (this) {
            PLAY -> R.drawable.ic_pause
            PAUSE -> R.drawable.ic_play
            else -> R.drawable.ic_play
        }
    }
}