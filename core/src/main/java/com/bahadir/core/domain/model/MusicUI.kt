package com.bahadir.core.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicUI(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: String,
    val contentUri: Uri
) : Parcelable
