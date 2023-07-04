package com.geekydroid.materialclock.ui.alarm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScreenContent

const val alarmScreenRoute = "alarm_route"

fun NavGraphBuilder.alarmScreen() {
    composable(
        route = alarmScreenRoute
    ) {
        AlarmScreenContent()
    }
}

