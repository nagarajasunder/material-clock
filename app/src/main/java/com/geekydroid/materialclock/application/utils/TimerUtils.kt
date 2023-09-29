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

    fun getTimerTextAfterDeletion(
        hour: Int,
        minute: Int,
        second: Int
    ) : Triple<Int,Int,Int> {



        val newHr = hour/10
        val hrReminder = hour%10

        val minQuotient = minute/10
        val newMin = (hrReminder*10) + minQuotient
        val minReminder = minute%10

        val secQuotient = second/10
        val newSec = (minReminder*10) + secQuotient

        return Triple(newHr,newMin,newSec)

    }

    fun getFormattedTimerTime(
        hour:Int,
        minute:Int,
        second: Int
    ): Triple<Int, Int, Int> {
        val newSec = second%60
        val extraMin = second/60

        var newMin = minute%60
        val extraHr = minute/60

        newMin+=extraMin
        val newHr = hour+extraHr

        return Triple(newHr,newMin,newSec)


    }
}
