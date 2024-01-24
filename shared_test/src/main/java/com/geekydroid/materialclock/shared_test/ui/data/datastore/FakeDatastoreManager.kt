package com.geekydroid.materialclock.shared_test.ui.data.datastore

import androidx.datastore.preferences.core.Preferences
import com.geekydroid.materialclock.application.datastore.DatastoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDatastoreManager : DatastoreManager {

    override suspend fun storeBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {

    }

    override suspend fun getBooleanValueOrNull(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return flow {
            emit(false)
        }
    }
}