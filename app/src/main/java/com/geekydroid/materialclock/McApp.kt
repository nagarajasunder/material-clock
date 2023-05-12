package com.geekydroid.materialclock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource


@Composable
fun McApp(
    appState: McAppState = rememberMcAppState()
) {
    Scaffold(
        bottomBar = {
            McBottomBar(bottomNavItems = appState.topLevelDestinations)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hello from Version catalogs")
        }
    }
}

@Composable
fun McBottomBar(
    bottomNavItems: List<BottomNavigationItems>
) {
    var selectedItem by remember {
        mutableStateOf(0)
    }
    NavigationBar {
        bottomNavItems.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = (selectedItem == index),
                onClick = {
                    selectedItem = index
                },
                label = {
                    Text(text = stringResource(id = bottomNavItem.itemName))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = bottomNavItem.itemIcon),
                        contentDescription = null
                    )
                },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}


