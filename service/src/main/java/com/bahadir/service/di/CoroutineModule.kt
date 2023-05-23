package com.bahadir.service.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
object CoroutineModule {
    @Provides
    @ServiceScoped
    @Named("MAIN")
    fun provideForegroundAppWatcher(
    ): CoroutineScope = CoroutineScope(Dispatchers.Main)
}