package com.geekydroid.materialclock.application.utils

object TimerUtils {



    fun getTimerTextBasedOnInput(
        input: Int,
        hour: Int,
        minute: Int,
        second: Int
    ) : Triple<Int,Int,Int> {

        if (hour > 9) {
            return Triple(hour,minute,second)
        }

        val sec = (second*10) + input
        val secQuotient : Int = (sec/100)
        val newSec = sec%100

        val min = (minute * 10) + secQuotient
        val minQuotient:Int = (min/100)
        val newMin = (min%100)

        val hr = (hour*10) + minQuotient
        val newHr = (hr%100)
        return Triple(newHr,newMin,newSec)


    }

}
