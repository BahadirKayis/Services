package com.bahadir.core.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.bahadir.core.common.ServiceName
import com.bahadir.core.domain.source.DataStoreDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DataStoreDataSourceImpl(private val context: Context) : DataStoreDataSource {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "com.bahadir.services")
    override suspend fun getServiceStatus(name: ServiceName): Boolean {
        return context.datastore.data.first()[booleanPreferencesKey(name.name)] ?: false
    }

    override fun getServiceStatusFlow(name: ServiceName): Flow<Boolean> = callbackFlow {
        context.datastore.data.onEach { preferences ->
            val value = preferences[booleanPreferencesKey(name.name)] ?: false
            trySend(value)
        }.launchIn(this)
        awaitClose { channel.close() }
    }

    override suspend fun setServiceStatus(status: Boolean, name: ServiceName) {
        context.datastore.edit { dataStore ->
            dataStore[booleanPreferencesKey(name.name)] = status
        }
    }
}