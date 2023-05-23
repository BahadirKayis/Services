package com.bahadir.core.di

import android.content.Context
import com.bahadir.core.data.source.UsageStateDataSourceImpl
import com.bahadir.core.domain.source.UsageStateDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsageStateModule {
    @Provides
    @Singleton
    fun provideUsageStateModuleProvider(@ApplicationContext context: Context): UsageStateDataSource =
        UsageStateDataSourceImpl(context)
}