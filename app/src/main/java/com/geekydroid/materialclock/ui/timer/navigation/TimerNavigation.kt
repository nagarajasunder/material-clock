package com.geekydroid.materialclock.ui.timer.navigation

import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.ui.timer.composables.TimerScreen

const val timerScreenRoute = "timer_route"

fun NavGraphBuilder.timerScreen() {
    composable(
        route = timerScreenRoute,
        deepLinks = listOf(
            NavDeepLink(
                uri = Constants.TIMER_NOTIFICATION_DEEP_LINK_URL
            )
        )
    ) {
        TimerScreen()
    }
}