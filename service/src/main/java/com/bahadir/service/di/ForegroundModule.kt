package com.bahadir.service.di

import android.content.Context
import com.bahadir.service.infrastructure.MusicControlImpl
import com.bahadir.service.infrastructure.NotificationControlImpl
import com.bahadir.service.provider.MusicControl
import com.bahadir.service.provider.NotificationControl
import com.bahadir.service.provider.ForegroundAppWatcher
import com.bahadir.service.infrastructure.ForegroundAppWatcherImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
object ForegroundModule {
    @Provides
    @ServiceScoped
    fun provideForegroundAppWatcher(
        @ApplicationContext context: Context,
        @Named("MAIN") coroutineScope: CoroutineScope
    ): ForegroundAppWatcher = ForegroundAppWatcherImpl(context, coroutineScope)

    @Provides
    @ServiceScoped
    fun provideNotificationControl(
        @ApplicationContext context: Context,
        notifi: NotificationControl
    ): MusicControl = MusicControlImpl(context, notifi)

}

@InstallIn(ServiceComponent::class)
@Module
interface
NotificationControlModule {
    @Binds
    fun bindNotificationControl(impl: NotificationControlImpl): NotificationControl

    @Binds
    fun provideMediaSession(@ApplicationContext context: Context): Context
}