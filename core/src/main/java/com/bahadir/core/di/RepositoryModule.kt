package com.bahadir.core.di

import android.content.Context
import com.bahadir.core.data.repository.ServicesRepositoryImpl
import com.bahadir.core.domain.repository.ServicesRepository
import com.bahadir.core.domain.source.DataStoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        dataSource: DataStoreDataSource,
        @ApplicationContext context: Context
    ): ServicesRepository =
        ServicesRepositoryImpl(dataSource, context)
}