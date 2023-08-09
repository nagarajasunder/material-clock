package com.geekydroid.materialclock.application.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
}