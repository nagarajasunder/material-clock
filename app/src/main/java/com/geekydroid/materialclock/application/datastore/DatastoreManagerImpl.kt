package com.geekydroid.materialclock.application.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatastoreManagerImpl @Inject constructor(
    private val prefs: DataStore<Preferences>,
) : DatastoreManager {

    override suspend fun storeBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {
        prefs.edit {
            it[key] = value
        }
    }

    override suspend fun getBooleanValueOrNull(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return prefs.data.catch { exception ->
            if (exception is IOException) {
                emptyPreferences()
            } else {
                throw exception
            }
        }.map { prefs ->
            prefs[key] ?: false
        }
    }

}