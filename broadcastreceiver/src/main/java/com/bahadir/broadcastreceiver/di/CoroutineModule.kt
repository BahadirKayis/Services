package com.bahadir.broadcastreceiver.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideForegroundAppWatcher(
    ): CoroutineScope = CoroutineScope(Dispatchers.IO)
}