package com.geekydroid.materialclock.application.datastore;

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DatastoreManager {


    suspend fun storeBooleanValue(key: Preferences.Key<Boolean>, value: Boolean)

    suspend fun getBooleanValueOrNull(key: Preferences.Key<Boolean>): Flow<Boolean>


}
