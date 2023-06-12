package com.bahadir.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    @Singleton
    @Named("IO")
    fun provideCoroutineIO(
    ): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Provides
    @Singleton
    @Named("MAIN")
    fun provideCoroutineMain(
    ): CoroutineScope = CoroutineScope(Dispatchers.Main)
}