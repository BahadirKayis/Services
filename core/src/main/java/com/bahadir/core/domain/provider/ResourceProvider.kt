package com.bahadir.core.domain.provider

import androidx.annotation.StringRes

interface ResourceProvider {
    fun string(@StringRes key: Int): String

}