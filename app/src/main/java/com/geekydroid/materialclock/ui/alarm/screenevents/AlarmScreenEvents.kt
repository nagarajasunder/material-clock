package com.geekydroid.materialclock.ui.alarm.screenevents

import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster

sealed interface AlarmScreenEvents {
    data class ScheduleAlarm(val alarmMaster: AlarmMaster) : AlarmScreenEvents

    data class CancelAlarm(val alarmId:Int) : AlarmScreenEvents

    data class CancelSnoozedAlarm(val alarmId:Int) : AlarmScreenEvents

    data class OpenAlarmSoundScreen(val alarmId:Int) : AlarmScreenEvents

}