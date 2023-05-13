package com.geekydroid.materialclock.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.compose.md_theme_dark_primaryContainer
import com.geekydroid.materialclock.navigation.BottomNavigationItems
import com.geekydroid.materialclock.navigation.McNavHost


@Composable
fun McApp(
    appState: McAppState = rememberMcAppState()
) {
    Scaffold(
        bottomBar = {
            McBottomBar(
                bottomNavItems = appState.topLevelDestinations,
                navigateToTopLevelDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination
            )
        }
    ) { paddingValues ->
        McNavHost(
            modifier = Modifier.padding(paddingValues),
            appState = appState
        )
    }
}

@Composable
fun McBottomBar(
    currentDestination: NavDestination?,
    bottomNavItems: List<BottomNavigationItems>,
    navigateToTopLevelDestination: (BottomNavigationItems) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = currentDestination.isTopLevelDestinationInStack(bottomNavItem),
                onClick = { navigateToTopLevelDestination(bottomNavItem) },
                label = {
                    Text(text = stringResource(id = bottomNavItem.itemName))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = bottomNavItem.itemIcon),
                        contentDescription = null
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = md_theme_dark_primaryContainer,

                    )
            )
        }
    }
}

fun NavDestination?.isTopLevelDestinationInStack(bottomNavItem: BottomNavigationItems): Boolean {
    return this?.hierarchy?.any { destination ->
        destination.route?.contains(bottomNavItem.itemRoute, true) ?: false
    } ?: false
}
