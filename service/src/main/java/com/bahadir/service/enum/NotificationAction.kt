package com.bahadir.service.enum

import com.bahadir.service.R

enum class NotificationAction() {
    NEXT, PREVIOUS, PLAY, EXIT, PAUSE;

    operator fun invoke(): Int {
        return when (this) {
            PLAY -> R.drawable.ic_pause
            PAUSE -> R.drawable.ic_play
            else -> R.drawable.ic_play
        }
    }
}