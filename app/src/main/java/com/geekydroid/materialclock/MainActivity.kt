package com.geekydroid.materialclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    Scaffold(
        bottomBar = {
            McBottomNavigation(
                destinations = appState.bottomNavScreens,
                currentDestination = appState.currentDestination,
                onNavigateClicked = appState::navigateToBottomNavScreen
            )
        }
    ) { paddingValues ->
        McNavHost(
            modifier = Modifier.padding(paddingValues),
            navController = appState.navController,
            startDestination = alarmScreenRoute
        )
    }

}