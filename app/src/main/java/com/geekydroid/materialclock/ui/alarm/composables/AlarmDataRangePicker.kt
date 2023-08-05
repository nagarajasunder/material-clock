package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerWrapper(
    modifier: Modifier = Modifier,
    titleText: String,
    onDateSelected: (Long, Long) -> Unit,
    onDismissed: () -> Unit
) {

    val dateRangePickerState = rememberDateRangePickerState()

    Column(modifier = modifier) {

        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onDismissed) {
                Image(Icons.Default.Close, contentDescription = "Close the date range picker")
            }
            TextButton(
                onClick = {
                    onDateSelected(
                        dateRangePickerState.selectedStartDateMillis!!,
                        dateRangePickerState.selectedEndDateMillis!!
                    )
                },
                enabled = dateRangePickerState.selectedEndDateMillis != null
            ) {
                Text(text = "Save")
            }
        }
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(text = titleText)
            }
        )
    }

}