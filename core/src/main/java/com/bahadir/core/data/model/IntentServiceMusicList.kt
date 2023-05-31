package com.bahadir.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IntentServiceMusicList(var position: Int, var musicList: List<MusicUI>) : Parcelable