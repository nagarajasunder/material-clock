package com.geekydroid.materialclock.ui.alarm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus

@Entity(tableName = "MC_ALARM_MASTER")
data class AlarmMaster(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("alarm_id")
    val alarmId: Int = 0,
    @ColumnInfo("alarm_status")
    val alarmStatus: AlarmStatus,
    @ColumnInfo("alarm_label")
    val alarmLabel: String,
    @ColumnInfo("alarm_time_millis")
    val alarmTimeInMillis: Long,
    @ColumnInfo("alarm_date_in_millis")
    val alarmDateInMillis: Long,
    @ColumnInfo("alarm_trigger_millis")
    val alarmTriggerMillis:Long = 0L,
    @ColumnInfo("alarm_scheduled_days")
    val alarmScheduledDays: String,
    @ColumnInfo("alarm_schedule_type")
    val alarmType: AlarmScheduleType,
    @ColumnInfo("is_alarm_vibrate")
    val isAlarmVibrate: Boolean,
    @ColumnInfo("alarm_sound_index")
    val alarmSoundIndex:Int,
    @ColumnInfo("is_snoozed")
    val isSnoozed: Boolean = false,
    @ColumnInfo("snooze_millis")
    val snoozeMillis:Long = 0L,
    @ColumnInfo("created_on")
    val createdOn: Long,
    @ColumnInfo("updated_on")
    val updatedOn: Long
)