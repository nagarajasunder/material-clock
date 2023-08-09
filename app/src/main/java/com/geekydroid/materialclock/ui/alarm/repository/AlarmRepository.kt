package com.geekydroid.materialclock.ui.alarm.repository

import com.geekydroid.materialclock.application.db.dao.AlarmDao
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val externalDispatcher: CoroutineDispatcher
) {

    fun getAllAlarms(): Flow<List<AlarmMaster>> = alarmDao.getAllAlarms()

    suspend fun insertNewAlarm(alarmMaster: AlarmMaster) {
        externalScope.launch(externalDispatcher) {
            alarmDao.insertAlarm(alarmMaster)
        }.join()
    }

    suspend fun updateExistingAlarm(alarmMaster: AlarmMaster) {
        externalScope.launch(externalDispatcher) {
            alarmDao.updateAlarm(alarmMaster)
        }
    }

    suspend fun deleteAlarmById(alarmId: Int) {
        externalScope.launch(externalDispatcher) {
            alarmDao.deleteAlarmById(alarmId)
        }
    }

}