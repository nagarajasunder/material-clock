package com.geekydroid.materialclock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.geekydroid.materialclock.ui.McAppState
import com.geekydroid.materialclock.alarm.navigation.ALARM_ROUTE
import com.geekydroid.materialclock.alarm.navigation.alarmScreen
import com.geekydroid.materialclock.stopwatch.navigation.stopWatchScreen
import com.geekydroid.materialclock.timer.navigation.timerScreen

@Composable
fun McNavHost(
    modifier: Modifier = Modifier,
    startDestination: String = ALARM_ROUTE,
    appState: McAppState
) {
    NavHost(
        modifier = modifier,
        navController = appState.navHostController,
        startDestination = startDestination
    ) {
        alarmScreen()
        stopWatchScreen()
        timerScreen()
    }
}