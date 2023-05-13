package com.geekydroid.materialclock.timer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.timer.TimerRoute

private const val TIMER_ROUTE = "timer"

fun NavHostController.navigateToTimerScreen(navOptions: NavOptions? = null) {
    this.navigate(TIMER_ROUTE,navOptions)
}

fun NavGraphBuilder.timerScreen() {
    composable(route = TIMER_ROUTE) {
        TimerRoute()
    }
}