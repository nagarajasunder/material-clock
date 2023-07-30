package com.geekydroid.materialclock.ui.alarm.model

import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus

/**
 * This class contains the data that are specific to Alarm
 */

data class AlarmUiData(
  val alarmLabel:String = "",
  val alarmTimeText:String = "",
  val alarmHour:Int = 0,
  val alarmMinute:Int = 0,
  val alarmTimeInMills:Long = 0L,
  val alarmStatus: AlarmStatus = AlarmStatus.OFF,
  val alarmScheduledDays: String = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
  val alarmScheduleText:String = "",
  val pauseAlarmText:String = "",
  val alarmSnoozeText:String = "",
  val showAlarmDismissCta:Boolean = false,
  val isAlarmVibrate:Boolean = false,
  val isAlarmExpanded:Boolean = true
) {
  companion object {
    val initialValue = AlarmUiData()
  }
}
