package com.geekydroid.materialclock.ui.alarm.model

import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus

/**
 * This class contains the data that are specific to Alarm
 */

data class AlarmUiData(
  val alarmId: Int = 0,
  val alarmLabel: String = "",
  val alarmTimeText: String = "",
  val alarmTimeInMills: Long = 0L,
  val alarmDateInMillis: Long = 0L,
  val alarmStatus: AlarmStatus = AlarmStatus.OFF,
  val alarmScheduleType: AlarmScheduleType = AlarmScheduleType.ONCE,
  val alarmScheduledDays: String = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
  val alarmScheduleText: String = "",
  val alarmSnoozeText: String = "",
  val showAlarmDismissCta: Boolean = false,
  val isAlarmVibrate: Boolean = false,
  val isAlarmSnooze:Boolean = false,
  val alarmSoundIndex:Int,
  val alarmSnoozeMillis:Long = 0L
)
