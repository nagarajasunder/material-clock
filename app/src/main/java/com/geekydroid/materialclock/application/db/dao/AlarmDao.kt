package com.geekydroid.materialclock.application.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Insert
    suspend fun insertAlarm(alarmMaster: AlarmMaster): Long

    @Update
    suspend fun updateAlarm(alarmMaster: AlarmMaster)

    @Query("SELECT * FROM MC_ALARM_MASTER")
    fun getAllAlarms(): Flow<List<AlarmMaster>>

    @Query("DELETE FROM MC_ALARM_MASTER WHERE alarm_id = :alarmId")
    suspend fun deleteAlarmById(alarmId: Int)

    @Query("UPDATE MC_ALARM_MASTER SET is_snoozed = :snoozed , alarm_status = :alarmStatus,  snooze_millis = :snoozeMillis WHERE alarm_id = :alarmId")
    suspend fun updateSnoozeDetails(alarmId: Int,alarmStatus: AlarmStatus,snoozed:Boolean,snoozeMillis:Long)

    @Query("UPDATE MC_ALARM_MASTER SET alarm_sound_index = :soundId WHERE alarm_id = :alarmId")
    suspend fun updateAlarmSoundId(alarmId: Int,soundId:Int)

    @Query("SELECT alarm_sound_index FROM MC_ALARM_MASTER WHERE alarm_id = :alarmId")
    fun getAlarmSoundId(alarmId:Int) : Flow<Int>
}