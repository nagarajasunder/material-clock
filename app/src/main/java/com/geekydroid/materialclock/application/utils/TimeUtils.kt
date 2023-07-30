package com.geekydroid.materialclock.application.utils

import android.app.AlarmManager
import java.util.Calendar

object TimeUtils {

    fun getTimeOneHourFromNow(): Pair<Int, Int> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        return Pair(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
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
}