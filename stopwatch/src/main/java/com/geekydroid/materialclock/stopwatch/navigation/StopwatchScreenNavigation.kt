package com.geekydroid.materialclock.stopwatch.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.stopwatch.StopwatchRoute

const val STOP_WATCH_ROUTE = "stopwatch"
fun NavGraphBuilder.stopWatchScreen() {
    composable(route = STOP_WATCH_ROUTE) {
        StopwatchRoute()
    }
}

fun NavController.navigateToStopwatchScreen(navOptions: NavOptions? = null) {
    this.navigate(STOP_WATCH_ROUTE,navOptions)
}