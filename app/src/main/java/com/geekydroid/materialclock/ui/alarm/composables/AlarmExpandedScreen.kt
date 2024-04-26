package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.utils.TIME_FORMATS
import com.geekydroid.materialclock.application.utils.TimeUtils
import java.util.concurrent.TimeUnit

@Composable
fun AlarmExpandedScreen(
    modifier: Modifier = Modifier,
    digitalTime: Long,
    alarmLabel: String,
    onSnoozeClick: () -> Unit,
    onDismissClick: () -> Unit
) {

    var showAlarmStatusText by remember {
        mutableStateOf(false)
    }
    var alarmStatusText by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(visible = showAlarmStatusText) {
            Text(
                text = alarmStatusText,
                style = MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }

        AnimatedVisibility(visible = !showAlarmStatusText) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.7f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = TimeUtils.getFormattedTime(TIME_FORMATS.HH_MM, digitalTime),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 36.sp
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.alarm),
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = alarmLabel,
                        style = MaterialTheme.typography.displaySmall.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Box(modifier = Modifier.fillMaxSize().weight(0.3f)) {
                    AlarmSwipper(
                        onSwipeLeft = {
                            alarmStatusText = "Alarm Snoozed"
                            showAlarmStatusText = true
                            onSnoozeClick()
                        },
                        onSwipeRight = {
                            alarmStatusText = "Alarm Dismissed"
                            showAlarmStatusText = true
                            onDismissClick()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AlarmExpandedScreenPreview() {
    AlarmExpandedScreen(digitalTime = System.currentTimeMillis(), alarmLabel = "Alarm", onSnoozeClick = { /*TODO*/ }) {

    }
}