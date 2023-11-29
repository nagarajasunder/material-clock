package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.geekydroid.materialclock.application.utils.TimerLogicHandler
import com.geekydroid.materialclock.ui.theme.McAppTheme
import com.geekydroid.materialclock.ui.timer.viewmodels.TimerViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TimerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var timerViewModel: TimerViewModel

    @Inject
    lateinit var timerLogicHandler: TimerLogicHandler


    @Test
    fun timerScreenInputTest() {
        /*
            Testing Flow
            Click Actions
                1. 05
                2. 15
                3. 74
                4. 1
         */
        hiltRule.inject()
        composeTestRule.setContent {
            timerViewModel = TimerViewModel(timerLogicHandler)
            McAppTheme {
                TimerScreen(timerViewModel = timerViewModel)
            }
        }
        //First Click
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("00h 00m 05s", useUnmergedTree = true).assertExists()
        //Second Click
        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithText("00h 05m 15s", useUnmergedTree = true).assertExists()
        //Third Click
        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("4").performClick()
        composeTestRule.onNodeWithText("05h 15m 74s", useUnmergedTree = true).assertExists()
        //Fourth Click
        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("51h 57m 41s", useUnmergedTree = true).assertExists()

    }

    @Test
    fun timerInputRemoveTest() {
        hiltRule.inject()
        composeTestRule.setContent {
            timerViewModel = TimerViewModel(timerLogicHandler)
            McAppTheme {
                TimerScreen(timerViewModel = timerViewModel)
            }
        }
        /**
         * Testing Flow
         * Click Actions
         *  1. Click 987543
         *  2. Delete until it becomes empty
         */
        composeTestRule.onNodeWithText("9", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("8", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("7", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("5", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("4", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("3", useUnmergedTree = true).performClick()
        composeTestRule.onNodeWithText("98h 75m 43s", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithContentDescription("backspace").performClick()
        composeTestRule.onNodeWithText("09h 87m 54s").assertExists()
        composeTestRule.onNodeWithContentDescription("backspace").performClick()
        composeTestRule.onNodeWithText("00h 98m 75s").assertExists()
        composeTestRule.onNodeWithContentDescription("backspace").performClick()
        composeTestRule.onNodeWithText("00h 09m 87s").assertExists()
        composeTestRule.onNodeWithContentDescription("backspace").performClick()
        composeTestRule.onNodeWithText("00h 00m 98s").assertExists()
        composeTestRule.onNodeWithContentDescription("backspace").performClick()
        composeTestRule.onNodeWithText("00h 00m 09s").assertExists()
        composeTestRule.onNodeWithContentDescription("backspace").performClick()
        composeTestRule.onNodeWithText("00h 00m 00s").assertExists()
        composeTestRule.onNodeWithText("start_timer").assertDoesNotExist()

    }

    @Test
    fun timerInput_start_button_hidden_at_start() {
        hiltRule.inject()
        composeTestRule.setContent {
            timerViewModel = TimerViewModel(timerLogicHandler)
            McAppTheme {
                TimerScreen(timerViewModel = timerViewModel)
            }
        }
        composeTestRule.onNodeWithContentDescription("start_timer").assertDoesNotExist()
    }

    @Test
    fun timerInput_start_button_visible_after_click() {
        hiltRule.inject()
        composeTestRule.setContent {
            timerViewModel = TimerViewModel(timerLogicHandler)
            McAppTheme {
                TimerScreen(timerViewModel = timerViewModel)
            }
        }
        composeTestRule.onNodeWithText("5").performClick()
        composeTestRule.onNodeWithContentDescription("start_timer").assertExists()
    }


    @Test
    fun timerInput_start_pause_timer_test() {
        hiltRule.inject()
        composeTestRule.setContent {
            timerViewModel = TimerViewModel(timerLogicHandler)
            McAppTheme {
                TimerScreen(timerViewModel = timerViewModel)
            }
        }
        composeTestRule.onNodeWithText("3").performClick()
        composeTestRule.onNodeWithContentDescription("start_timer").assertExists()
        composeTestRule.onNodeWithContentDescription("start_timer").performClick()
        composeTestRule.onNodeWithContentDescription("Pause").assertExists()
        composeTestRule.onNodeWithContentDescription("Pause").performClick()
        composeTestRule.onNodeWithContentDescription("Pause").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("+1:00").assertDoesNotExist()
    }

}