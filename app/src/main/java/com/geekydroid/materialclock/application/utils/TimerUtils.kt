package com.geekydroid.materialclock.application.utils

import java.lang.StringBuilder

object TimerUtils {
    fun getTimerTextBasedOnInput(
        input: Int,
        hour: Int,
        minute: Int,
        second: Int
    ): Triple<Int, Int, Int> {

        if (hour > 9) {
            return Triple(hour, minute, second)
        }

        val sec = (second * 10) + input
        val secQuotient: Int = (sec / 100)
        val newSec = sec % 100

        val min = (minute * 10) + secQuotient
        val minQuotient: Int = (min / 100)
        val newMin = (min % 100)

        val hr = (hour * 10) + minQuotient
        val newHr = (hr % 100)
        return Triple(newHr, newMin, newSec)


    }

    fun getTimerTextAfterDeletion(
        hour: Int,
        minute: Int,
        second: Int
    ): Triple<Int, Int, Int> {


        val newHr = hour / 10
        val hrReminder = hour % 10

        val minQuotient = minute / 10
        val newMin = (hrReminder * 10) + minQuotient
        val minReminder = minute % 10

        val secQuotient = second / 10
        val newSec = (minReminder * 10) + secQuotient

        return Triple(newHr, newMin, newSec)

    }

    fun getFormattedTimerTime(
        hour: Int,
        minute: Int,
        second: Int
    ): Triple<Int, Int, Int> {
        val newSec = second % 60
        val extraMin = second / 60

        val newMin = (minute + extraMin) % 60
        val extraHr = (minute + extraMin) / 60

        val newHr = hour + extraHr

        return Triple(newHr, newMin, newSec)


    }

    fun getTimerTextBasedOnSeconds(seconds: Int): String {
        val hours = seconds / 3600
        val hrText = if (hours < 10) "0${hours}" else hours.toString()
        val remMinutes = seconds % 3600
        val minutes = remMinutes / 60
        val minText = if (minutes < 10) "0${minutes}" else minutes.toString()
        val remSeconds = remMinutes % 60
        val secondsText = if (remSeconds < 10) "0${remSeconds}" else remSeconds.toString()
        val result = StringBuilder()
        if (hours > 0) {
            result.append("$hrText:")
        }
        if (minutes > 0) {
            result.append("${minText}:")
        }
        result.append(secondsText)

        return result.toString()
    }

    fun convertTimerTextToSeconds(
        hour: Int,
        minute: Int,
        seconds: Int
    ): Int {
        val hourToSec = (hour * 3600)
        val minToSec = (minute * 60)
        return (hourToSec + minToSec + seconds)
    }

    fun getTimerProgress(
        actualTimerSec: Int,
        currentTimerSec: Int
    ): Float {
        return (currentTimerSec.toFloat()/actualTimerSec.toFloat())
    }
}
