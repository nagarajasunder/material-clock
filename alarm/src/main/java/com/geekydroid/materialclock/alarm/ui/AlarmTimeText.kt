package com.geekydroid.materialclock.alarm.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AlarmTimeText(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean
) {
    val fontWeight = if (enabled) FontWeight.ExtraBold else FontWeight.Normal
    AnimatedContent(targetState = fontWeight) { fontWeightAnimated ->
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.displaySmall.copy(
                letterSpacing = TextUnit(
                    1f,
                    TextUnitType.Sp
                )
            ),
            fontWeight = fontWeightAnimated
        )
    }
}