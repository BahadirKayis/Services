package com.bahadir.core.infrastructure

import android.content.Context
import com.bahadir.core.domain.provider.ResourceProvider


class ResourceProviderImpl(val context: Context) : ResourceProvider {
    override fun string(key: Int): String = context.getString(key)
}