package com.geekydroid.materialclock.shared_test.di

import android.content.Context
import androidx.room.Room
import com.geekydroid.materialclock.application.datastore.DatastoreManager
import com.geekydroid.materialclock.application.db.AppDatabase
import com.geekydroid.materialclock.application.di.DatabaseModule
import com.geekydroid.materialclock.application.di.RepositoryModule
import com.geekydroid.materialclock.shared_test.ui.data.datastore.FakeDatastoreManager
import com.geekydroid.materialclock.shared_test.ui.data.repository.FakeAlarmRepository
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton


@Module
@TestInstallIn(
    components =[SingletonComponent::class],
    replaces =[RepositoryModule::class]
)
object TestRepositoryModule {


    @Singleton
    @Provides
    fun providesAlarmRepository(database: AppDatabase) : AlarmRepository {
        return AlarmRepositoryImpl(database.alarmDao(), CoroutineScope(SupervisorJob()), Dispatchers.IO)
    }

    @Provides
    @Singleton
    fun providesDatastore() : DatastoreManager {
        return FakeDatastoreManager()
    }

}