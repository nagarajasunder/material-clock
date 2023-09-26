package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R

@Composable
fun OkButtonDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onOkClicked: () -> Unit
) {

    AlertDialog(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onOkClicked() }) {
                Text(text = stringResource(id = R.string.ok_label))
            }
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        }
    )
}