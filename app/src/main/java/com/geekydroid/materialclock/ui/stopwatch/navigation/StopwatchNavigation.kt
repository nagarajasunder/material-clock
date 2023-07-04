package com.geekydroid.materialclock.ui.stopwatch.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.ui.stopwatch.composables.StopwatchScreen

const val stopWatchScreenRoute = "stopwatch_router"

fun NavGraphBuilder.stopwatchScreen() {
    composable(
        route = stopWatchScreenRoute
    ) {
        StopwatchScreen()
    }
}