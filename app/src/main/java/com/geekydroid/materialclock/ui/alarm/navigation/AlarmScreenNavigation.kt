package com.geekydroid.materialclock.ui.alarm.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScreenContent

const val alarmScreenRoute = "alarm_route"

fun NavGraphBuilder.alarmScreen(navHostController: NavHostController) {
    composable(
        route = alarmScreenRoute
    ) {
        AlarmScreenContent(navHostController = navHostController)
    }
}

