package com.geekydroid.materialclock.application.utils

import android.app.AlarmManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeUtils {

    fun getOneHourFromNowInMillis(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        return cal.timeInMillis
    }

    fun isTomorrow(timeInMills:Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,0)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        return (calendar.timeInMillis + AlarmManager.INTERVAL_DAY) < timeInMills
    }

    fun isPastTime(timeInMills: Long) : Boolean = System.currentTimeMillis() > timeInMills

    fun getFormattedTime(timeFormat: TIME_FORMATS,timeInMillis:Long) : String {
        val timeFormatStr = when(timeFormat) {
            TIME_FORMATS.HH_MM -> "HH:mm"
            TIME_FORMATS.MMM_DD -> "MMM dd"
        }
        return SimpleDateFormat(timeFormatStr, Locale.ENGLISH).format(timeInMillis)
    }

    fun getHourMinuteFromMillis(millis:Long) : Pair<Int,Int> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return Pair(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE))
    }
}

enum class TIME_FORMATS {
    HH_MM,
    MMM_DD
}