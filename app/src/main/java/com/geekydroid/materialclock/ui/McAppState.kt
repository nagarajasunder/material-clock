package com.geekydroid.materialclock.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.geekydroid.materialclock.navigation.BottomNavigationItems
import com.geekydroid.materialclock.alarm.navigation.navigateToAlarmScreen
import com.geekydroid.materialclock.stopwatch.navigation.navigateToStopwatchScreen
import com.geekydroid.materialclock.timer.navigation.navigateToTimerScreen
import kotlinx.coroutines.CoroutineScope


data class McAppState(
    val coroutineScope: CoroutineScope,
    val navHostController: NavHostController
) {
    val topLevelDestinations: List<BottomNavigationItems> = BottomNavigationItems.values().asList()

    val currentDestination: NavDestination?
        @Composable get() = navHostController.currentBackStackEntryAsState().value?.destination

    fun navigateToTopLevelDestination(destination: BottomNavigationItems) {
        val topLevelNavOptions = navOptions {
            popUpTo(navHostController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (destination) {
            BottomNavigationItems.ALARM -> {
                navHostController.navigateToAlarmScreen(topLevelNavOptions)
            }

            BottomNavigationItems.TIMER -> {
                navHostController.navigateToTimerScreen(topLevelNavOptions)
            }

            BottomNavigationItems.STOPWATCH -> {
                navHostController.navigateToStopwatchScreen(topLevelNavOptions)
            }
        }
    }
}

@Composable
fun rememberMcAppState(
    scope: CoroutineScope = rememberCoroutineScope(),
    navHost: NavHostController = rememberNavController()
) = remember(
    scope,
    navHost
) {
    McAppState(scope, navHost)
}