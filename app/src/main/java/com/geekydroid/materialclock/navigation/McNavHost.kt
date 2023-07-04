package com.geekydroid.materialclock.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.geekydroid.materialclock.ui.alarm.navigation.alarmScreen
import com.geekydroid.materialclock.ui.stopwatch.navigation.stopwatchScreen
import com.geekydroid.materialclock.ui.timer.navigation.timerScreen

@Composable
fun McNavHost(
    modifier:Modifier = Modifier,
    navController: NavHostController,
    startDestination:String
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        builder = {
            alarmScreen()
            stopwatchScreen()
            timerScreen()
        }
    )

}