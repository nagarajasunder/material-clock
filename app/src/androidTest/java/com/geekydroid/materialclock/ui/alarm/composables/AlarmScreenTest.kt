package com.geekydroid.materialclock.ui.alarm.composables

import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.isNotFocused
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekydroid.materialclock.MainActivity
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.datastore.DatastoreManager
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.application.utils.ResourceProvider
import com.geekydroid.materialclock.rememberMcAppState
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import com.geekydroid.materialclock.ui.alarm.viewmodel.AlarmViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTestApi::class,ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AlarmScreenTest {



    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)

    @get:Rule
    val hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var alarmRepository: AlarmRepository

    private lateinit var viewModel:AlarmViewModel

    @Inject
    lateinit var resourceProvider: ResourceProvider


    @Inject
    lateinit var datastoreManager : DatastoreManager

    @Inject
    @IoDispatcher
    lateinit var testDispatcher:CoroutineDispatcher

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = AlarmViewModel(
            resourceProvider = resourceProvider,
            datastoreManager = datastoreManager,
            alarmRepository = alarmRepository,
            externalDispatcher = testDispatcher
        )
    }

    private fun placeComposeForTest() {
        composeTestRule.activity.setContent {
            val appState = rememberMcAppState()
            AlarmScreenContent(navHostController = appState.navController, viewModel = viewModel)
        }
    }


    private fun createBasicAlarm() {
        val addButtonLabel = composeTestRule.activity.getString(R.string.add_alarm)
        val dialogOkTestTag = composeTestRule.activity.getString(R.string.dialog_ok_test_tag)
        composeTestRule.onNodeWithContentDescription(addButtonLabel).performClick()
        composeTestRule.onNodeWithTag(dialogOkTestTag).assertExists()
        composeTestRule.onNodeWithTag(dialogOkTestTag).performClick()
    }

    @Test
    fun alarm_screen_creation_alarm_card_should_displayed() = runTest {
        /**
         * Testing Flow
         * -> Click aad new alarm
         * -> Select the default time and Add Alarm
         * -> Select add Label & provide some input
         * -> Select schedule days
         * -> Turn off the alarm
         * -> Delete the alarm
         */
        placeComposeForTest()
        createBasicAlarm()
        val alarmCardTestTag = composeTestRule.activity.getString(R.string.alarm_card_test_tag)
        composeTestRule.onNodeWithTag(alarmCardTestTag).assertExists()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(alarmCardTestTag), 500)
        composeTestRule.onNodeWithTag(alarmCardTestTag).assertExists()
    }

    @Test
    fun alarm_add_label_test_label_should_be_added() = runTest {
        placeComposeForTest()
        createBasicAlarm()
        val addLabelText = composeTestRule.activity.getString(R.string.add_label)
        val dialogOkTestTag = composeTestRule.activity.getString(R.string.dialog_ok_test_tag)
        val labelText = composeTestRule.activity.getString(R.string.label)
        composeTestRule.onNodeWithText(addLabelText, useUnmergedTree = true).performClick()
        val labelInputNode = composeTestRule.onNode(
            isFocusable()
                    and
                    hasText(labelText)
        )
        labelInputNode.performClick()
        labelInputNode.performKeyInput {
            this.keyDown(Key.L)
            this.keyUp(Key.L)
            this.keyDown(Key.A)
            this.keyUp(Key.A)
            this.keyDown(Key.B)
            this.keyUp(Key.B)
            this.keyDown(Key.E)
            this.keyUp(Key.E)
        }
        composeTestRule.onNodeWithTag(dialogOkTestTag, useUnmergedTree = true).performClick()
        composeTestRule.onNode(
            isNotFocused()
                    and
                    hasText("labe")
        ).assertExists()
    }

    @Test
    fun alarm_schedule_day_selection_test() = runTest {
        placeComposeForTest()
        createBasicAlarm()
        val alarmCardTestTag = composeTestRule.activity.getString(R.string.alarm_card_test_tag)
        val alarmExpandCollapseTag = composeTestRule.activity.getString(R.string.expand_or_collapse)
        composeTestRule.onNodeWithContentDescription(alarmExpandCollapseTag).performClick()
        /**
         * The day selector field is the 5th indexed child of the UI semantics tree in alarm card
         * so we have used index 5 to select it.
         * After that each weekday is indexed from 0 to 6
         * If you have any doubts in the future just print the semantics tree to LOG
         *
         */
        val weekDaySelectionNode = composeTestRule.onNode(hasTestTag(alarmCardTestTag))
            .onChildAt(5)
        weekDaySelectionNode.onChildAt(0)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Sun"), 500)
        composeTestRule.onNodeWithText("Sun", ignoreCase = true).assertExists()
        weekDaySelectionNode.onChildAt(1)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Sun,Mon"), 500)
        composeTestRule.onNodeWithText("Sun,Mon", ignoreCase = true).assertExists()
        weekDaySelectionNode.onChildAt(2)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Sun,Mon,Tue"), 500)
        composeTestRule.onNodeWithText("Sun,Mon,Tue", ignoreCase = true).assertExists()
        weekDaySelectionNode.onChildAt(3)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Sun,Mon,Tue,Wed"), 500)
        composeTestRule.onNodeWithText("Sun,Mon,Tue,Wed", ignoreCase = true).assertExists()
        weekDaySelectionNode.onChildAt(4)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Sun,Mon,Tue,Wed,Thu"), 500)
        composeTestRule.onNodeWithText("Sun,Mon,Tue,Wed,Thu", ignoreCase = true).assertExists()
        weekDaySelectionNode.onChildAt(5)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Sun,Mon,Tue,Wed,Thu,Fri"), 500)
        composeTestRule.onNodeWithText("Sun,Mon,Tue,Wed,Thu,Fri", ignoreCase = true).assertExists()
        weekDaySelectionNode.onChildAt(6)
            .performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("Everyday"), 500)
        composeTestRule.onNodeWithText("Everyday", ignoreCase = true).assertExists()
    }

    @Test
    fun alarm_on_off_toggle_test() = runTest {
        placeComposeForTest()
        createBasicAlarm()
        val alarmCardTestTag = composeTestRule.activity.getString(R.string.alarm_card_test_tag)
        val alarmExpandCollapseTag = composeTestRule.activity.getString(R.string.expand_or_collapse)
        composeTestRule.onNodeWithContentDescription(alarmExpandCollapseTag).performClick()
        val toggleSwitch = composeTestRule.onNodeWithTag(alarmCardTestTag)
            .onChildren()
            .filter(isToggleable())
            .onFirst()
        toggleSwitch.assertIsOn()
        toggleSwitch.performClick()
        toggleSwitch.assertIsOff()
    }

    @Test
    fun alarm_delete_test_alarm_should_be_deleted() = runTest {
        placeComposeForTest()
        createBasicAlarm()
        val alarmExpandCollapseTag = composeTestRule.activity.getString(R.string.expand_or_collapse)
        composeTestRule.onNodeWithContentDescription(alarmExpandCollapseTag).performClick()
        composeTestRule.onNodeWithText("Delete", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete", ignoreCase = true).performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasText("No Alarms"),500)
        composeTestRule.onNodeWithText("No Alarms").assertIsDisplayed()
    }
}