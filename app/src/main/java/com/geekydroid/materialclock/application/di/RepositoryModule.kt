package com.geekydroid.materialclock.application.di

import com.geekydroid.materialclock.application.datastore.DatastoreManager
import com.geekydroid.materialclock.application.datastore.DatastoreManagerImpl
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsAlarmRepository(alarmRepositoryImpl: AlarmRepositoryImpl) : AlarmRepository

    @Binds
    abstract fun bindsDatastoreManager(datastoreManagerImpl: DatastoreManagerImpl) : DatastoreManager
}