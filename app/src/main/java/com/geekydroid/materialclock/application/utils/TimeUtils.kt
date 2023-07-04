package com.geekydroid.materialclock.application.utils

import java.util.Calendar

object TimeUtils {

    fun getTimeOneHourFromNow(): Pair<Int, Int> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        return Pair(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
    }
}