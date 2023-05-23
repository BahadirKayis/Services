package com.bahadir.core.di

import android.content.Context
import com.bahadir.core.data.repository.OverlayServiceRepositoryImpl
import com.bahadir.core.domain.repository.OverlayServiceRepository
import com.bahadir.core.domain.source.DataStoreDataSource
import com.bahadir.core.domain.source.UsageStateDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    fun provideRepository(
        dataSource: DataStoreDataSource,
        usageState: UsageStateDataSource,
        @ApplicationContext context: Context
    ): OverlayServiceRepository =
        OverlayServiceRepositoryImpl(dataSource, usageState, context)
}