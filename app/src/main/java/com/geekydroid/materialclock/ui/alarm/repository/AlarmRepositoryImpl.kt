package com.geekydroid.materialclock.ui.alarm.repository

import com.geekydroid.materialclock.application.db.dao.AlarmDao
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmRepositoryImpl @Inject constructor(
    private val alarmDao: AlarmDao,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val externalDispatcher: CoroutineDispatcher
) : AlarmRepository {

    override fun getAllAlarms(): Flow<List<AlarmMaster>> = alarmDao.getAllAlarms()

    override suspend fun getAllActiveAlarms(): List<AlarmMaster> = alarmDao.getAllActiveAlarms()

    override suspend fun insertNewAlarm(alarmMaster: AlarmMaster) : Long {
        return withContext(externalScope.coroutineContext + externalDispatcher) {
            alarmDao.insertAlarm(alarmMaster)
        }
    }

    override suspend fun updateExistingAlarm(alarmMaster: AlarmMaster) {
       externalScope.launch(externalDispatcher) {
           alarmDao.updateAlarm(alarmMaster)
       }
    }

    override suspend fun updateSnoozeDetails(alarmId:Int, alarmStatus: AlarmStatus, isSnoozed:Boolean, snoozeMillis:Long) {
        externalScope.launch {
            alarmDao.updateSnoozeDetails(alarmId,alarmStatus,isSnoozed,snoozeMillis)
        }
    }

    override suspend fun deleteAlarmById(alarmId: Int) {
        externalScope.launch(externalDispatcher) {
            alarmDao.deleteAlarmById(alarmId)
        }
    }

    override suspend fun updateAlarmSoundId(alarmId: Int, soundId:Int) {
        externalScope.launch(externalDispatcher) {
            alarmDao.updateAlarmSoundId(alarmId,soundId)
        }
    }

    override fun getAlarmSoundId(alarmId: Int) : Flow<Int> {
        return alarmDao.getAlarmSoundId(alarmId)
    }

}