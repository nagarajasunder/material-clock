package com.geekydroid.materialclock

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
import com.geekydroid.materialclock.ui.alarm.navigation.alarmScreenRoute
import com.geekydroid.materialclock.ui.stopwatch.navigation.stopWatchScreenRoute
import com.geekydroid.materialclock.ui.timer.navigation.timerScreenRoute
import kotlinx.coroutines.CoroutineScope

class McAppState(
    val coroutineScope: CoroutineScope,
    val navController: NavHostController
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val bottomNavScreens: List<BottomNavigationItems> = BottomNavigationItems.values().asList()

    fun navigateToBottomNavScreen(bottomNavScreen: BottomNavigationItems) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            /**
             * This config is to avoid creating multiple copies of same item
             */
            launchSingleTop = true
            restoreState = true

        }
        when (bottomNavScreen) {
            BottomNavigationItems.ALARM -> navController.navigate(alarmScreenRoute, navOptions)
            BottomNavigationItems.TIMER -> navController.navigate(timerScreenRoute, navOptions)
            BottomNavigationItems.STOPWATCH -> navController.navigate(
                stopWatchScreenRoute,
                navOptions
            )
        }
    }

}

@Composable
fun rememberMcAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navHostController: NavHostController = rememberNavController()
): McAppState =
    remember(
        coroutineScope,
        navHostController
    ) {
        McAppState(coroutineScope, navHostController)
    }
