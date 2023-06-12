package com.bahadir.service.di

import android.content.Context
import com.bahadir.service.domain.provider.MusicControl
import com.bahadir.service.domain.provider.NotificationControl
import com.bahadir.service.domain.provider.SoundControl
import com.bahadir.service.infrastructure.CustomAccelerometerEventListener
import com.bahadir.service.infrastructure.MusicControlImpl
import com.bahadir.service.infrastructure.NotificationControlImpl
import com.bahadir.service.infrastructure.SoundControlImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object InfrastructureModule {

    @Provides
    @ServiceScoped
    internal fun provideNotificationControl(
        @ApplicationContext context: Context, notification: NotificationControl
    ): MusicControl = MusicControlImpl(context, notification)

    @Provides
    @ServiceScoped
    internal fun provideSoundControl(
        @ApplicationContext context: Context
    ): SoundControl = SoundControlImpl(context)

    @Provides
    @ServiceScoped
    fun provideSensorEvent(
        @ApplicationContext context: Context
    ) = CustomAccelerometerEventListener(context)


    @Provides
    @ServiceScoped
    internal fun provideNotification(
        @ApplicationContext context: Context
    ): NotificationControl = NotificationControlImpl(context)
}