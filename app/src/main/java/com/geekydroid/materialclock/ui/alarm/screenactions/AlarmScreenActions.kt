package com.geekydroid.materialclock.ui.alarm.screenactions

import java.util.Calendar

interface AlarmScreenActions {

    fun onAddAlarmClicked()

    fun onAlarmTimeChanged(calendar: Calendar,showTimeInput:Boolean)

    fun onAlarmScheduledDatesChanged()

    fun onLabelValueChange(newValue:String)

    fun onAlarmLabelDialogDismissed()

    fun onLabelValueConfirmed()

    fun onTimePickerDialogClosed()

    fun onDatePickerDialogDismissed()

    fun onDatePickerConfirmed(time:Long)

    fun onDateRangePickerConfirmed(timePair:Pair<Long,Long>)
}