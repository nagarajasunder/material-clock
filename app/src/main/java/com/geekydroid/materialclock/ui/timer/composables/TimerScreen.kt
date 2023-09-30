package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState
import com.geekydroid.materialclock.ui.timer.viewmodels.TimerViewModel

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel = hiltViewModel()
) {

    val timerState by timerViewModel.timerScreenState.collectAsStateWithLifecycle()
    TimerScreenContent(
        modifier.padding(8.dp),
        state = timerState,
        onTimerInputChanged = timerViewModel::onTimerInputChanged,
        onTimerStartClicked = timerViewModel::onTimerStartClicked
    )

}

@Composable
fun TimerScreenContent(
    modifier: Modifier = Modifier,
    state: TimerScreenState,
    onTimerInputChanged: (String) -> Unit,
    onTimerStartClicked: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Timer",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
        if (state.timerStarted) {
            TimerCard(
                timerLabel = state.timerEvent.timerLabel,
                timerText = state.timerEvent.timerText,
                timerProgress = state.timerEvent.timerProgress
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