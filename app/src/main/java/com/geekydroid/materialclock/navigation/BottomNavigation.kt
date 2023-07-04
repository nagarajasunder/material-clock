package com.geekydroid.materialclock.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun McBottomNavigation(
    destinations:List<BottomNavigationItems>,
    currentDestination:NavDestination?,
    onNavigateClicked: (BottomNavigationItems) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        destinations.forEach { item ->
            val selected = currentDestination.currentDestinationSelected(item)
            NavigationBarItem(
                selected = selected,
                onClick = {
                    onNavigateClicked(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.itemIcon),
                        contentDescription = item.itemRoute
                    )
                }
            )
        }
    }
}

fun NavDestination?.currentDestinationSelected(destination:BottomNavigationItems) : Boolean {
   return this?.hierarchy?.any {
       it.route?.contains(destination.itemRoute,true) ?: false
   } ?: false
}