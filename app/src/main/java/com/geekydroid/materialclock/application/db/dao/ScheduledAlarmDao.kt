package com.geekydroid.materialclock.application.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.geekydroid.materialclock.ui.alarm.model.ScheduledAlarm

@Dao
interface ScheduledAlarmDao {

    @Insert
    suspend fun insertScheduledAlarm(scheduledAlarm: ScheduledAlarm)

    @Query("SELECT * FROM MC_SCHEDULED_ALARM WHERE SCHEDULED_ALARM_ID = :id")
    suspend fun getScheduledAlarmBasedOnId(id:Long) : ScheduledAlarm
}