package com.bahadir.core.data.repository

import android.content.Context
import android.provider.MediaStore
import com.bahadir.core.common.Resource
import com.bahadir.core.common.ServiceName
import com.bahadir.core.common.formatDuration
import com.bahadir.core.data.model.MusicUI
import com.bahadir.core.domain.repository.ServicesRepository
import com.bahadir.core.domain.source.DataStoreDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class ServicesRepositoryImpl(
    private val dataStore: DataStoreDataSource,
    private val context: Context
) : ServicesRepository {
    override suspend fun setServiceStatus(status: Boolean, name: ServiceName) =
        dataStore.setServiceStatus(status, name)

    override suspend fun getServiceStatus(name: ServiceName): Boolean =
        dataStore.getServiceStatus(name)

    override fun getServiceStatusFlow(name: ServiceName): Flow<Boolean> = callbackFlow {
        dataStore.getServiceStatusFlow(name).onEach { trySend(it) }.launchIn(this)
        awaitClose { channel.close() }
    }

    override fun getMusicList(): Flow<Resource<List<MusicUI>>> = flow {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.MIME_TYPE
        )
        val selection = "${MediaStore.Audio.Media.MIME_TYPE}=?"
        //Sadece mp3 dosyalarını almak için MIME_TYPE'ı "audio/mpeg" olanları seçiyoruz.
        val selectionArgs = arrayOf("audio/mpeg")
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)

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
            emit(Resource.Success(musicList))
        }
    }
}