package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.ui.theme.weekDaySelectedColor

@Composable
fun DaySelectorField(
    modifier: Modifier = Modifier,
    weekDaySelectedText:String = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
    notifyWeekDaySelectionChange: (WeekDay) -> Unit
) {

    BasicTextField(
        modifier = modifier,
        enabled = false,
        value = TextFieldValue("", selection = TextRange(0)),
        onValueChange = {},
        decorationBox = {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                WeekDay.values().forEachIndexed { index, weekDay ->
                    val isSelected = weekDaySelectedText[index].isUpperCase()
                    val backgroundColor =
                        if (isSelected) weekDaySelectedColor else Color.Unspecified
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .padding(4.dp)
                            .border(0.5.dp, if (isSelected) weekDaySelectedColor else Color.LightGray, CircleShape)
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .clickable {
                                notifyWeekDaySelectionChange(weekDay)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = weekDay.label,
                                style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center,
                                color = if (isSelected) Color.Black else Color.Unspecified)
                            )
                        }
                    }
                }
            }
        }

    )
}


enum class WeekDay(val label: String,val value:String,val index:Int) {
    SUNDAY(label = "S", value = "Sun", index = 0),
    MONDAY(label = "M", value = "Mon", index = 1),
    TUESDAY(label = "T",  value = "Tue",index = 2),
    WEDNESDAY(label = "W",  value = "Wed",index = 3),
    THURSDAY(label = "T",  value = "Thu",index = 4),
    FRIDAY(label = "F",  value = "Fri",index = 5),
    SATURDAY(label = "S", value = "Sat", index = 6)
}