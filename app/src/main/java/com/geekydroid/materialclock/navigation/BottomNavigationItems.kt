package com.geekydroid.materialclock.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.geekydroid.materialclock.R as appResources
import com.geekydroid.materialclock.designsystem.R as designSystemResource

enum class BottomNavigationItems(
    @StringRes val itemName: Int,
    val itemRoute: String,
    @DrawableRes val itemIcon: Int
) {

    ALARM(
        com.geekydroid.materialclock.R.string.alarm,
        "alarm",
        designSystemResource.drawable.baseline_access_alarm_24
    ),

    TIMER(
        appResources.string.timer,
        "timer",
        designSystemResource.drawable.baseline_hourglass_24
    ),

    STOPWATCH(
        appResources.string.stopwatch,
        "stopwatch",
        designSystemResource.drawable.baseline_timer_24
    ),


}