package com.geekydroid.materialclock.ui.timer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.ui.timer.composables.TimerScreen

const val timerScreenRoute = "timer_route"

fun NavGraphBuilder.timerScreen() {
    composable(
        route = timerScreenRoute
    ) {
        TimerScreen()
    }
}