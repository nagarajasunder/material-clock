package com.geekydroid.materialclock.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.geekydroid.materialclock.R

enum class BottomNavigationItems(
    @StringRes val itemName: Int,
    val itemRoute: String,
    @DrawableRes val itemIcon: Int
) {

    ALARM(
        R.string.alarm,
        "alarm",
        R.drawable.baseline_access_alarm_24
    ),

    TIMER(
        R.string.timer,
        "timer",
        R.drawable.baseline_hourglass_24
    ),

    STOPWATCH(
        R.string.stopwatch,
        "stopwatch",
        R.drawable.baseline_timer_24
    ),



}