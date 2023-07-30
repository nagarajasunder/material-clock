package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.geekydroid.materialclock.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogWrapper(
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit,
    onConfirmed: (Long) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismissed,
        confirmButton = {
            TextButton(
                onClick = { onConfirmed(datePickerState.selectedDateMillis!!) },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = stringResource(id = R.string.ok_label))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissed,
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}