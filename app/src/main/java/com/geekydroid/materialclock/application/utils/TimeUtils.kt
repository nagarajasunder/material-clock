package com.geekydroid.materialclock.application.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TAG = "TimeUtils"

object TimeUtils {

    fun getOneHourFromNowInMillis(): Long {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        return cal.timeInMillis
    }

    fun isTomorrow(timeInMills:Long): Boolean {
        return getFormattedTime(TIME_FORMATS.DD_MM_YYYY,timeInMills) != getFormattedTime(TIME_FORMATS.DD_MM_YYYY,System.currentTimeMillis())
    }

    fun isPastTime(timeInMills: Long) : Boolean = System.currentTimeMillis() > timeInMills

    fun getFormattedTime(timeFormat: TIME_FORMATS,timeInMillis:Long) : String {
        val timeFormatStr = when(timeFormat) {
            TIME_FORMATS.HH_MM -> "HH:mm"
            TIME_FORMATS.MMM_DD_YYYY -> "MMM dd, yyyy"
            TIME_FORMATS.EEE_HH_MM -> "EEE, HH:mm a"
            TIME_FORMATS.DD_MM_YYYY -> "dd/MM/yyyy"
            TIME_FORMATS.YYYY -> "yyyy"
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
    MMM_DD_YYYY,
    EEE_HH_MM,
    DD_MM_YYYY,
    YYYY
}