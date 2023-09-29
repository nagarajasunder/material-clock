package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.ui.theme.timerInputSelectedColor

@Composable
fun TimerStartButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Button(
        modifier = modifier,
        onClick = onClick, shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = timerInputSelectedColor
        )
    ) {
        Icon(
            modifier = Modifier.padding(12.dp),
            painter = painterResource(id = R.drawable.baseline_play_arrow_24),
            contentDescription = null
        )
    }

}

@Preview(showBackground = true)
@Composable
fun TimerStartButtonPreview() {
    TimerStartButton(){}
}