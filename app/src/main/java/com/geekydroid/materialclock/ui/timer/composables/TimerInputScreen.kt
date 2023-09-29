package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.ui.theme.md_theme_dark_outlineVariant
import com.geekydroid.materialclock.ui.theme.timerInputRemovalColor
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState

@Composable
fun TimerInputScreen(
    modifier: Modifier = Modifier,
    state: TimerScreenState,
    onTimerInputChanged: (String) -> Unit,
    onTimerStartClick: () -> Unit
) {

    val numbers = listOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "0", "X",
    )
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            TimerTextComponent(
                hour = state.timerHr,
                minute = state.timerMin,
                second = state.timerSec
            )
        }
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
                .background(Color.Yellow),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
                    .background(Color.Blue),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.Center,
            ) {
                items(numbers) { number ->
                    TimerInputButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),
                        text = number,
                        color = if (number == "X") timerInputRemovalColor else md_theme_dark_outlineVariant,
                        iconRes = if (number == "X") R.drawable.backspace else null,
                        onClick = onTimerInputChanged
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = state.showStartTimer) {
                FloatingActionButton(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(8.dp),
                    onClick = onTimerStartClick,
                    shape = CircleShape
                ) {
                    Icon(
                        modifier = Modifier.padding(12.dp),
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = null
                    )
                }
            }
        }
    }

}