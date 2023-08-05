package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.utils.TimeUtils
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogWrapper(
    initialTimeMillis:Long = System.currentTimeMillis(),
    showPickerInitial: Boolean = true,
    onConfirm: (Calendar,Boolean) -> Unit,
    onCancel: () -> Unit
) {
    val hourMinutePair = TimeUtils.getHourMinuteFromMillis(initialTimeMillis)
    val state = rememberTimePickerState(initialHour = hourMinutePair.first, initialMinute = hourMinutePair.second)
    val showingPicker = remember { mutableStateOf(showPickerInitial) }
    val configuration = LocalConfiguration.current

    TimePickerDialog(
        title = if (showingPicker.value) {
            "Select Time "
        } else {
            "Enter Time"
        },
        onCancel = onCancel,
        onConfirm = {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, state.hour)
            cal.set(Calendar.MINUTE, state.minute)
            cal.isLenient = false
            /**
             * If user select a particular form of time input we need to store that preference
             * and show the same type of time input next time as well
             */
            onConfirm(cal,(!showingPicker.value))
        },
        toggle = {
            if (configuration.screenHeightDp > 400) {
                // Make this take the entire viewport. This will guarantee that Screen readers
                // focus the toggle first.
                IconButton(
                    modifier = Modifier
                        // This is a workaround so that the Icon comes up first
                        // in the talkback traversal order. So that users of a11y
                        // services can use the text input. When talkback traversal
                        // order is customizable we can remove this.
                        .size(64.dp, 72.dp),
                    onClick = { showingPicker.value = !showingPicker.value }) {
                    val icon = if (showingPicker.value) {
                        painterResource(id = R.drawable.baseline_keyboard_24)
                    } else {
                        painterResource(id = R.drawable.baseline_schedule_24)
                    }
                    Icon(
                        icon,
                        contentDescription = if (showingPicker.value) {
                            "Switch to Text Input"
                        } else {
                            "Switch to Touch Input"
                        }
                    )
                }
            }
        }
    ) {
        if (showingPicker.value && configuration.screenHeightDp > 400) {
            TimePicker(state = state)
        } else {
            TimeInput(state = state)
        }
    }

}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            /**
             * If we didn't set this to false then the TimePicker's width
             * (which is huge) will be set for TimeInput as well
             */
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        toggle()
                    }
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}
