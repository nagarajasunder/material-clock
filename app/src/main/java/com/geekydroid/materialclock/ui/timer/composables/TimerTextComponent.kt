package com.geekydroid.materialclock.ui.timer.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.geekydroid.materialclock.ui.theme.timerInputSelectedColor
import com.geekydroid.materialclock.ui.theme.timerTextStyle

@Composable
fun TimerTextComponent(
    modifier: Modifier = Modifier,
    hour: Int,
    minute: Int,
    second: Int
) {

    val hourColor = if (hour == 0) Color.Gray else timerInputSelectedColor
    val minuteColor = if (minute > 0 || hour > 0) timerInputSelectedColor else Color.Gray
    val secondsColor = if (second > 0 || minute > 0 || hour > 0) timerInputSelectedColor else Color.Gray

    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = hourColor)) {
                    append(if (hour < 10) "0${hour}" else hour.toString())
                }
                withStyle(style = SpanStyle(color = hourColor, fontSize = 22.sp)) {
                    append("h")
                }
                append(" ")
                withStyle(style = SpanStyle(color = minuteColor)) {
                    append(if (minute < 10) "0${minute}" else minute.toString())
                }
                withStyle(style = SpanStyle(color = minuteColor, fontSize = 22.sp)) {
                    append("m")
                }
                append(" ")
                withStyle(style = SpanStyle(color = secondsColor)) {
                    append(if (second < 10) "0${second}" else second.toString())
                }
                withStyle(style = SpanStyle(color = secondsColor, fontSize = 22.sp)) {
                    append("s")
                }
            },
            style = timerTextStyle.copy(color = Color.Gray)
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun TimerTextComponentPreview() {
}