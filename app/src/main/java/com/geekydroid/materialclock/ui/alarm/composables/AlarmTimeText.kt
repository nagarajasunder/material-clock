package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AlarmTimeText(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (enabled) Color.White else Color.Unspecified
    val fontWeight = if (enabled) FontWeight.ExtraBold else FontWeight.Normal
    Text(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {onClick()},
        text = text,
        style = MaterialTheme.typography.displaySmall.copy(
            letterSpacing = TextUnit(
                1f,
                TextUnitType.Sp
            )
        ),
        color = textColor,
        fontWeight = fontWeight
    )
}