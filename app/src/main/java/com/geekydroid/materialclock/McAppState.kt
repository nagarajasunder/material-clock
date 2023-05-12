package com.geekydroid.materialclock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope


data class McAppState(
    val coroutineScope: CoroutineScope,
    val navHostController: NavHostController
) {
    val topLevelDestinations:List<BottomNavigationItems> = BottomNavigationItems.values().asList()
}

@Composable
fun rememberMcAppState(
    scope: CoroutineScope = rememberCoroutineScope(),
    navHost: NavHostController = rememberNavController()
) = remember(
    scope,
    navHost
) {
    McAppState(scope,navHost)
}