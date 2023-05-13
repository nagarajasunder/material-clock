package com.geekydroid.materialclock.alarm.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.designsystem.theme.md_theme_light_onPrimaryContainer
import com.geekydroid.materialclock.designsystem.theme.md_theme_light_secondary

@Composable
internal fun WeekRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Weekdays.values().asList().forEach {
            WeekRowItem(weekDay = it)
        }
    }
}

@Composable
internal fun WeekRowItem(
    modifier: Modifier = Modifier,
    weekDay: Weekdays
) {
    val backgroundColor by animateColorAsState(targetValue = if (weekDay.isSelected) md_theme_light_secondary else Color.Unspecified)

    Box(
        modifier = modifier
            .size(36.dp)
            .border(border = BorderStroke(1.dp, color = Color.Gray), CircleShape)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(4.dp),
            text = weekDay.label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}

enum class Weekdays(val label: String, var isSelected: Boolean) {
    SUNDAY(label = "S", false),
    MONDAY(label = "M", false),
    TUESDAY(label = "T", true),
    WEDNESDAY(label = "W", false),
    THURSDAY(label = "T", false),
    FRIDAY(label = "F", false),
    SATURDAY(label = "S", true),
}