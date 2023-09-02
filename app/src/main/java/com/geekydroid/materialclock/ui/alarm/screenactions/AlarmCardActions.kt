package com.geekydroid.materialclock.ui.alarm.screenactions

import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.composables.WeekDay

interface AlarmCardActions {

    fun onAlarmTimeTextClicked(index:Int)
    fun onAlarmCollapsedChanged(index: Int)
    fun onAddLabelClicked(index: Int)
    fun onAlarmStatusChange(index: Int, newStatus: AlarmStatus)
    fun onAlarmScheduledDaysChange(index: Int, selectedDay: WeekDay)
    fun onScheduleAlarmClicked(index: Int)
    fun onAlarmSoundChange(index:Int)
    fun onVibrationStatusChange(index: Int,newStatus:Boolean)
    fun onSnoozeCancelled(index:Int)
    fun onDeleteClicked(index: Int)
}