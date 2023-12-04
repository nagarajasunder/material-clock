package com.geekydroid.materialclock.ui

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.geekydroid.materialclock.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    /**
     * Why am i creating this type of createAndroidComposeRule?
     * Because i need the specific implementation as of MainActivity to run this test,
     * so I'm using this
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun appBottomNavigationTest() {
//        hiltRule.inject()
//        composeTestRule.activity.setContent {
//            McAppTheme {
//
//                McApp(appState = rememberMcAppState())
//            }
//        }
//        composeTestRule.onNodeWithContentDescription("timer").performClick()
//        composeTestRule.onNodeWithContentDescription("timer").assertIsSelected()
//        composeTestRule.onNodeWithContentDescription("alarm").assertIsNotSelected()
//        composeTestRule.onNodeWithContentDescription("alarm").performClick()
//        composeTestRule.onNodeWithContentDescription("alarm").assertIsSelected()
    }

}