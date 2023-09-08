package com.geekydroid.materialclock.application.utils


import android.app.AlarmManager
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import java.util.Calendar

private const val TAG = "AlarmUtils"


object AlarmUtils {


    fun getAlarmTimeBasedOnConstraints(
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays:String,
        alarmTimeMillis: Long,
        alarmDateMillis:Long
    ) : Long {

        /**
         * The logic behind this, Let's say an alarm has rang yesterday at 07:00 and it is off now
         * and Today at 08:00 the user again turns On the alarm so the time for today is already past so we are
         * scheduling the alarm for next day
         */
        val finalAlarmTimeMillis = if (alarmTimeMillis < System.currentTimeMillis())
            getSameTimeInMillisForNextDay(alarmTimeMillis)
        else
            alarmTimeMillis

        val result =  when(alarmScheduleType) {
            AlarmScheduleType.ONCE -> {
                finalAlarmTimeMillis
            }
            AlarmScheduleType.SCHEDULE_ONCE -> {
                getDateTimeFromDateAndTime(alarmDateMillis, finalAlarmTimeMillis)
            }
            AlarmScheduleType.REPEATED -> {
                getAlarmTimeForRepeatedAlarm(alarmScheduleDays,finalAlarmTimeMillis)
            }
        }
        return result
    }

    private fun getSameTimeInMillisForNextDay(alarmTimeMillis: Long): Long {
        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = alarmTimeMillis
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND))
        return calendar.timeInMillis + AlarmManager.INTERVAL_DAY
    }

    fun getDateTimeFromDateAndTime(dateMillis: Long, timeMillis: Long): Long {
        val dateCalendar = Calendar.getInstance()
        dateCalendar.timeInMillis = dateMillis
        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = timeMillis
        dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
        dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
        dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND))
        dateCalendar.set(Calendar.MILLISECOND, timeCalendar.get(Calendar.MILLISECOND))
        return dateCalendar.timeInMillis
    }


    /**
     * Logic
     * Input
     * Schedule Days -> SmtWtfs
     * the capital letters indicate the days that the alarm should be repeated
     * We need to get the current week day index(0 to 6) and iterate it and check whether the current day is selected if not add interval
     * of 1 day in millis(86_400_000) till we get the day in upper case
     */

    private fun getAlarmTimeForRepeatedAlarm(alarmScheduleDays:String,alarmTimeMillis:Long) : Long {
        val calendar = Calendar.getInstance()
        var currentDayIndex = calendar[Calendar.DAY_OF_WEEK]
        var scheduleTime = System.currentTimeMillis()
        if (alarmScheduleDays[currentDayIndex].isUpperCase()) {
            return alarmTimeMillis
        }
        val intervalDay = AlarmManager.INTERVAL_DAY
        var alarmDateMillis = 0L
        for (i in (0 until Constants.NO_OF_DAYS_IN_A_WEEK)) {
            val index = currentDayIndex % Constants.NO_OF_DAYS_IN_A_WEEK
            val day = alarmScheduleDays[index]
            if (day.isUpperCase()) {
                scheduleTime += intervalDay
                alarmDateMillis = scheduleTime
                break
            } else {
                scheduleTime += intervalDay
            }
            currentDayIndex += 1
        }
        return getDateTimeFromDateAndTime(alarmDateMillis,alarmTimeMillis)
    }

    fun isEligibleForReminderAlarm(alarmTriggerMillis:Long) : Boolean {
        return alarmTriggerMillis > (System.currentTimeMillis() + (Constants.ALARM_REMINDER_HOUR*AlarmManager.INTERVAL_HOUR))
    }

    fun getAlarmSnoozeTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        return calendar.timeInMillis + Constants.ALARM_SNOOZE_INTERVAL_MILLIS
    }



}