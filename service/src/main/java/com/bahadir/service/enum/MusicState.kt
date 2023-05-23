package com.bahadir.service.enum

import android.os.Parcelable
import com.bahadir.service.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MusicState : Parcelable {
    PLAY {
        override fun getDrawableResource(): Int {
            return R.drawable.ic_pause
        }
    },
    PAUSE {
        override fun getDrawableResource(): Int {
            return R.drawable.ic_play
        }
    };

    //    operator fun not(): Int {
//        return when (this) {
//            PLAY -> R.drawable.ic_pause
//            PAUSE -> R.drawable.ic_play
//        }
//    }
    abstract fun getDrawableResource(): Int
}