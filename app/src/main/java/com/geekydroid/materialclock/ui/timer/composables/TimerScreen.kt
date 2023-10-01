package com.geekydroid.materialclock.ui.timer.composables

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.services.TimerService
import com.geekydroid.materialclock.ui.timer.models.TimerEvent
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState
import com.geekydroid.materialclock.ui.timer.models.TimerState
import com.geekydroid.materialclock.ui.timer.screenevents.TimerScreenEvents
import com.geekydroid.materialclock.ui.timer.viewmodels.TimerViewModel

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val timerState by timerViewModel.timerScreenState.collectAsStateWithLifecycle()
    val timerEvent by timerViewModel.timerEvent.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        timerViewModel.events.collect { event ->
            when(event) {
                is TimerScreenEvents.startTimerService -> {
                    val intent = Intent(context,TimerService::class.java)
                    intent.putExtra(Constants.TIMER_EVENT_BUNDLE,event.timerEvent)
                    intent.putExtra(Constants.TIMER_ACTION_TYPE,Constants.TIMER_ACTION_START_TIMER)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent)
                    }
                    else {
                        context.startService(intent)
                    }
                }
            }
        }
    }
    TimerScreenContent(
        modifier.padding(8.dp),
        state = timerState,
        onTimerInputChanged = timerViewModel::onTimerInputChanged,
        onTimerStartClicked = timerViewModel::onTimerStartClicked,
        timerEventState = timerEvent,
        onAddOneMinuteClicked = timerViewModel::onAddOneMinuteClicked,
        onPauseTimerClicked = timerViewModel::onPauseTimerClicked,
        onResumeClicked = timerViewModel::onResumeClicked,
        onResetTimerClicked = timerViewModel::onResetTimerClicked,
        onCloseTimerClicked = timerViewModel::onCloseTimerClicked
    )

}

@Composable
fun TimerScreenContent(
    modifier: Modifier = Modifier,
    state: TimerScreenState,
    timerEventState:TimerEvent,
    onTimerInputChanged: (String) -> Unit,
    onTimerStartClicked: () -> Unit,
    onAddOneMinuteClicked: () -> Unit,
    onPauseTimerClicked: () -> Unit,
    onResumeClicked: () -> Unit,
    onResetTimerClicked: () -> Unit,
    onCloseTimerClicked: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Timer",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (timerEventState.timerState != TimerState.IDLE) {
            TimerCard(
                timerLabel = timerEventState.timerLabel,
                timerText = timerEventState.timerText,
                timerProgress = timerEventState.timerProgress,
                timerState = timerEventState.timerState,
                onAddOneMinuteClicked = onAddOneMinuteClicked,
                onPauseTimerClicked = onPauseTimerClicked,
                onResumeClicked = onResumeClicked,
                onResetTimerClicked = onResetTimerClicked,
                onCloseTimerCLicked = onCloseTimerClicked
            )
        }
        else {
            TimerInputScreen(
                state = state,
                onTimerInputChanged = onTimerInputChanged,
                onTimerStartClick = onTimerStartClicked
            )
        }

    }
}