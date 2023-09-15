package com.geekydroid.materialclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.navigation.McBottomNavigation
import com.geekydroid.materialclock.navigation.McNavHost
import com.geekydroid.materialclock.ui.alarm.navigation.alarmScreenRoute
import com.geekydroid.materialclock.ui.theme.McAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            McAppTheme {
                McApp(appState = rememberMcAppState())
            }
        }
    }
}

@Composable
fun McApp(
    appState: McAppState
) {

    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val hideBottomBar = navBackStackEntry?.arguments?.getBoolean(Constants.ARG_HIDE_BOTTOM_BAR) ?: false
    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                McBottomNavigation(
                    destinations = appState.bottomNavScreens,
                    currentDestination = appState.currentDestination,
                    onNavigateClicked = appState::navigateToBottomNavScreen
                )
            }
        }
    ) { paddingValues ->
        McNavHost(
            modifier = Modifier.padding(paddingValues),
            navController = appState.navController,
            startDestination = alarmScreenRoute
        )
    }

}