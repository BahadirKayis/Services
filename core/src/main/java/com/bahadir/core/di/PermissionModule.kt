package com.bahadir.core.di

import android.content.Context
import com.bahadir.core.domain.provider.PermissionProvider
import com.bahadir.core.infrastructure.PermissionProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PermissionModule {
    @Provides
    @Singleton
    fun providePermissionManager(@ApplicationContext context: Context): PermissionProvider =
        PermissionProviderImpl(context)
}