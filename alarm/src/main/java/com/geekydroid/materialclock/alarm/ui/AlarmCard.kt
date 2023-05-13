package com.geekydroid.materialclock.alarm.ui

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.alarm.R

@Composable
internal fun AlarmCard(
    modifier: Modifier = Modifier
) {

    var checked by remember {
        mutableStateOf(false)
    }

    Card(modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .clickable { }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = com.geekydroid.materialclock.designsystem.R.drawable.outline_label_24),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = stringResource(id = R.string.add_label)
                    )
                }
                Icon(
                    modifier = Modifier
                        .rotate(180f)
                        .clip(CircleShape)
                        .clickable { }
                        .padding(4.dp),
                    painter = painterResource(id = com.geekydroid.materialclock.designsystem.R.drawable.outline_label_24),
                    contentDescription = null
                )
            }
            AlarmTimeText(text = "23:00", enabled = checked)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Not Scheduled")
                Switch(checked = checked, onCheckedChange = {
                    checked = !checked
                })
            }
            WeekRow(modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_MASK)
fun AlarmCardPreview() {
    Column(modifier = Modifier.padding(8.dp)) {
        AlarmCard()
    }
}