package com.geekydroid.materialclock.alarm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.alarm.AlarmRoute

const val ALARM_ROUTE = "alarm"


fun NavController.navigateToAlarmScreen(navOptions: NavOptions? = null) {
    this.navigate(ALARM_ROUTE, navOptions)
}

fun NavGraphBuilder.alarmScreen() {
    composable(
        route = ALARM_ROUTE
    ) {
        AlarmRoute()
    }
}