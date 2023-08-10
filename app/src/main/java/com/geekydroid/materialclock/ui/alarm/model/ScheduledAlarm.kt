package com.geekydroid.materialclock.ui.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("MC_SCHEDULED_ALARM")
data class ScheduledAlarm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "SCHEDULED_ALARM_ID")
    val scheduledAlarmId: Long,
    @ColumnInfo(name = "ALARM_MASTER_ID")
    val alarmMasterId: Long,
    @ColumnInfo(name = "ALARM_TIME_MILLIS")
    val alarmTimeMillis: Long,
    @ColumnInfo(name = "ALARM_TYPE")
    val alarmType: AlarmType
)