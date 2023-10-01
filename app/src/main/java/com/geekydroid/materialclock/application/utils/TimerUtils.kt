package com.geekydroid.materialclock.application.utils

import android.util.Log
import java.lang.StringBuilder
import kotlin.math.abs

private const val TAG = "TimerUtils"

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

    //3715000
    private fun getHrMinSecFromMillis(millis: Long): Triple<Int,Int,Int> {
        val hours = millis / 36_00_000
        val remMinutes = millis % 36_00_000
        val minutes = remMinutes / 60_000
        val remSeconds = remMinutes % 60_000
        val seconds = remSeconds / 1000
        return Triple(hours.toInt(),minutes.toInt(),seconds.toInt())
    }

    fun getTimerTextBasedOnMillis(millis: Long): String {
        val absMillis = abs(millis)
        val hrMinSec = getHrMinSecFromMillis(absMillis)
        val hours = hrMinSec.first
        val minutes = hrMinSec.second
        val seconds = hrMinSec.third
        Log.d(TAG, "getTimerTextBasedOnMillis: millis $millis $hrMinSec")
        val hrText = hours.toString()
        val minText = if (hours > 0 && minutes < 9) {
            "0${minutes}"
        }
        else {
            minutes.toString()
        }
        val secondsText = if (minutes > 0 && seconds in (0..9)) "0${seconds}" else seconds.toString()
        val result = StringBuilder()
        if (millis < 0) {
            result.append("-")
        }
        if (hours > 0) {
            result.append("$hrText:")
        }
        if (minutes > 0 || hours > 0) {
            result.append("${minText}:")
        }
        result.append(secondsText)
        return result.toString()
    }

    fun getTimerTextLabelBasedOnMillis(millis: Long) : String {
        val absMillis = abs(millis)
        val hrMinSec = getHrMinSecFromMillis(absMillis)
        val hours = hrMinSec.first
        val minutes = hrMinSec.second
        val seconds = hrMinSec.third

        val hrText = hours.toString()
        val minText = minutes.toString()
        val secondsText = seconds.toString()
        val result = StringBuilder()
        if (hours > 0) {
            result.append("${hrText}h ")
        }
        if (minutes > 0) {
            result.append("${minText}m ")
        }
        if (seconds > 0) {
            result.append("${secondsText}s ")
        }
        return result.toString()
    }

    fun convertTimerTextToMillis(
        hour: Int,
        minute: Int,
        seconds: Int
    ): Long {
        val hourToMillis = (hour * 36_00_000L)
        val minToMillis = (minute * 60_000L)
        val secToMillis = (seconds*1000L)
        return (hourToMillis + minToMillis + secToMillis)
    }

    fun getTimerProgress(
        actualTimerSec: Long,
        currentTimerSec: Long
    ): Float {
        return (currentTimerSec.toFloat()/actualTimerSec.toFloat())
    }
}
