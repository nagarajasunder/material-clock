package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLabelDialog(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onLabelConfirmed: () -> Unit,
    onDialogDismissed: () -> Unit
) {

    AlertDialog(onDismissRequest = onDialogDismissed) {
        Surface(
            modifier = modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value, onValueChange = onValueChange,
                    placeholder = {
                        Text(text = stringResource(id = R.string.label))
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onDialogDismissed() }) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = stringResource(id = R.string.cancel),
                            style = TextStyle.Default.copy(color = Color(0xFF4FA4E6))
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onLabelConfirmed() }) {
                        Text(
                            modifier = Modifier
                                .testTag(stringResource(id = R.string.dialog_ok_test_tag))
                                .padding(4.dp),
                            text = stringResource(id = R.string.ok_label),
                            style = TextStyle.Default.copy(
                                color = Color(0xFF4FA4E6)
                            )
                        )
                    }
                }
            }

        }
    }

}