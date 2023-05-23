package com.bahadir.core.data.repository

import android.content.Context
import android.provider.MediaStore
import com.bahadir.core.common.Resource
import com.bahadir.core.common.formatDuration
import com.bahadir.core.domain.model.MusicUI
import com.bahadir.core.domain.model.UsageStateUI
import com.bahadir.core.domain.repository.OverlayServiceRepository
import com.bahadir.core.domain.source.DataStoreDataSource
import com.bahadir.core.domain.source.UsageStateDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

@Singleton
class OverlayServiceRepositoryImpl(
    private val dataStore: DataStoreDataSource,
    private val usageState: UsageStateDataSource,
    private val context: Context
) : OverlayServiceRepository {
    override suspend fun setServiceStatus(status: Boolean) {
        dataStore.setServiceStatus(status)
    }

    override suspend fun getServiceStatus(): Boolean = dataStore.getServiceStatus()


    override suspend fun setServiceStartTime(startTime: Long) {
        dataStore.setServiceStartTime(startTime)
    }

    override fun getUsageStatesTime(): Flow<Resource<List<UsageStateUI>>> = callbackFlow {
        val startTime = dataStore.getServiceStartTime()
        val data = usageState.getUsageStatesTime(startTime)
        trySend(Resource.Success(data))
        awaitClose { channel.close() }
    }

    override fun getMusicList(): Flow<Resource<List<MusicUI>>> = callbackFlow {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        cursor?.use { useCursor ->
            val idColumn = useCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = useCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistColumn = useCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = useCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            val musicList = mutableListOf<MusicUI>()
            while (useCursor.moveToNext()) {
                val id = useCursor.getLong(idColumn)
                val name = useCursor.getString(nameColumn)
                val artist = useCursor.getString(artistColumn)
                val duration = useCursor.getLong(durationColumn)
                val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon()
                    .appendPath(id.toString()).build()

                musicList.add(MusicUI(id, name, artist, duration.formatDuration(), contentUri))
            }

            trySend(Resource.Success(musicList))
            awaitClose { channel.close() }
        }
    }
}