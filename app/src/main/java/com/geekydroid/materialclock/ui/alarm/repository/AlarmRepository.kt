package com.geekydroid.materialclock.ui.alarm.repository

import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAllAlarms(): Flow<List<AlarmMaster>>

    suspend fun getAllActiveAlarms(): List<AlarmMaster>

    suspend fun insertNewAlarm(alarmMaster: AlarmMaster) : Long

    suspend fun updateExistingAlarm(alarmMaster: AlarmMaster)

    suspend fun updateSnoozeDetails(alarmId:Int, alarmStatus: AlarmStatus, isSnoozed:Boolean, snoozeMillis:Long)

    suspend fun deleteAlarmById(alarmId: Int)

    suspend fun updateAlarmSoundId(alarmId: Int,soundId:Int)

    fun getAlarmSoundId(alarmId: Int) : Flow<Int>
}