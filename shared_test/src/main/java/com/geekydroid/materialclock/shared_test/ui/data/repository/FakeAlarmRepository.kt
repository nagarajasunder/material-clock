package com.geekydroid.materialclock.shared_test.ui.data.repository

import com.geekydroid.materialclock.shared_test.ui.data.mock.MockData
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FakeAlarmRepository : AlarmRepository {

    override fun getAllAlarms(): Flow<List<AlarmMaster>> {
        return MockData.getAlarms()
    }

    override suspend fun getAllActiveAlarms(): List<AlarmMaster> {
       return listOf()
    }

    override suspend fun insertNewAlarm(alarmMaster: AlarmMaster): Long {
        val alarmId = MockData.getAlarms().first().size+1
        MockData.addAlarm(alarmMaster.copy(alarmId = alarmId))
        return alarmId.toLong()
    }

    override suspend fun updateExistingAlarm(alarmMaster: AlarmMaster) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSnoozeDetails(
        alarmId: Int,
        alarmStatus: AlarmStatus,
        isSnoozed: Boolean,
        snoozeMillis: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlarmById(alarmId: Int) {
        MockData.deleteAlarm(alarmId)
    }

    override suspend fun updateAlarmSoundId(alarmId: Int, soundId: Int) {
        TODO("Not yet implemented")
    }

    override fun getAlarmSoundId(alarmId: Int): Flow<Int> {
        TODO("Not yet implemented")
    }
}