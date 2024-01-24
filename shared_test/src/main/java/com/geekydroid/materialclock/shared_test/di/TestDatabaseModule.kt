package com.geekydroid.materialclock.shared_test.di

import android.content.Context
import androidx.room.Room
import com.geekydroid.materialclock.application.db.AppDatabase
import com.geekydroid.materialclock.application.db.dao.AlarmDao
import com.geekydroid.materialclock.application.db.dao.ScheduledAlarmDao
import com.geekydroid.materialclock.application.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class TestDatabaseModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun providesAlarmDao(database: AppDatabase): AlarmDao {
        return database.alarmDao()
    }

    @Provides
    @Singleton
    fun providesScheduledAlarmDao(database: AppDatabase) : ScheduledAlarmDao {
        return database.scheduledAlarmDao()
    }


}

