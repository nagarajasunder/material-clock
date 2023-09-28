package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.utils.TimerUtils
import com.geekydroid.materialclock.ui.theme.md_theme_dark_outlineVariant
import com.geekydroid.materialclock.ui.theme.timerInputRemovalColor

@Composable
fun TimerInputScreen(
    modifier: Modifier = Modifier
) {

    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "0", "X")
    var timerInput by remember {
        mutableStateOf(Triple(0,0,0))
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            verticalArrangement = Arrangement.Center
        ) {
            TimerTextComponent(
                hour = timerInput.first,
                minute = timerInput.second,
                second = timerInput.third
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(numbers) { number ->
                    TimerInputButton(
                        modifier = Modifier.size(100.dp),
                        text = number,
                        color = if (number == "X") timerInputRemovalColor else md_theme_dark_outlineVariant,
                        iconRes = if (number == "X") R.drawable.backspace else null,
                        onClick = {
                            timerInput = if (it == "X") {
                                TimerUtils.getTimerTextAfterDeletion(
                                    hour = timerInput.first,
                                    minute = timerInput.second,
                                    second = timerInput.third
                                )
                            } else {
                                TimerUtils.getTimerTextBasedOnInput(
                                    input = it.toInt(),
                                    hour = timerInput.first,
                                    minute = timerInput.second,
                                    second = timerInput.third
                                )
                            }
                        }
                    )
                }
            }
        }
    }

}


@Preview
@Composable
fun TimerInputScreenPreview() {
    TimerInputScreen()
}