package com.geekydroid.materialclock.application.di

import android.content.Context
import androidx.room.Room
import com.geekydroid.materialclock.application.db.AppDatabase
import com.geekydroid.materialclock.application.db.dao.AlarmDao
import com.geekydroid.materialclock.application.db.dao.ScheduledAlarmDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "mc_database").build()
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