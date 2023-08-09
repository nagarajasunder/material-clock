package com.geekydroid.materialclock.application.db.typeconverters

import androidx.room.TypeConverter
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType

class AlarmScheduleTypeConverter {

    @TypeConverter
    fun scheduleTypeToString(alarmScheduleType: AlarmScheduleType): String {
        return when (alarmScheduleType) {
            AlarmScheduleType.ONCE -> AlarmScheduleType.ONCE.name
            AlarmScheduleType.SCHEDULE_ONCE -> AlarmScheduleType.SCHEDULE_ONCE.name
            AlarmScheduleType.REPEATED -> AlarmScheduleType.REPEATED.name
        }
    }

    @TypeConverter
    fun stringToAlarmScheduleType(alarmScheduleTypeStr: String): AlarmScheduleType {
        AlarmScheduleType.values().forEach {
            if (it.name == alarmScheduleTypeStr) {
                return it
            }
        }
        return AlarmScheduleType.ONCE
    }
}