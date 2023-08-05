package com.geekydroid.materialclock.ui.alarm.screenevents

sealed interface AlarmScreenEvents {

    object OpenDateRangePicker : AlarmScreenEvents
    object OpenDatePicker : AlarmScreenEvents

}