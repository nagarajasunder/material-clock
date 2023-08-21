package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.utils.TIME_FORMATS
import com.geekydroid.materialclock.application.utils.TimeUtils
import com.geekydroid.materialclock.ui.theme.alarmCardContainerColor
import com.geekydroid.materialclock.ui.theme.weekDaySelectedColor
import com.geekydroid.materialclock.ui.theme.week_day_highlight_color

enum class AlarmStatus {
    ON,
    OFF
}
enum class AlarmScheduleType {
    /**
     * @property ONCE -> Alarm Rings only once Today/Tomorrow
     */
    ONCE,
    /**
     * @property SCHEDULE_ONCE -> Alarm Rings on a particular date
     */
    SCHEDULE_ONCE,

    /**
     * @property REPEATED -> Alarm Rings in a repeatable manner (Every Sunday,Monday or Everyday)
     */
    REPEATED
}

/**
 * index
 * label
 * onAddLabelClicked
 * onCollapseClicked
 * onAlarmStatusChange
 * onDaySelected
 * onScheduleAlarmClicked
 * onVibrateStatusChange
 * onDeleteClick
 */
@Composable
fun AlarmCard(
    modifier: Modifier = Modifier,
    label: String,
    alarmTime: String,
    alarmStatus: AlarmStatus,
    alarmScheduleDays:String,
    alarmScheduleText: String,
    alarmScheduleType: AlarmScheduleType,
    vibrateStatus: Boolean,
    alarmExpanded: Boolean,
    isSnoozed : Boolean,
    alarmSnoozeMillis : Long,
    onAddLabelClicked: () -> Unit,
    onCollapseClicked: () -> Unit,
    onAlarmTimeClick: () -> Unit,
    onAlarmStatusChange: (AlarmStatus) -> Unit,
    onDaysSelected: (WeekDay) -> Unit,
    onScheduleAlarmClicked: () -> Unit,
    onVibrateStatusChange: (Boolean) -> Unit,
    onSnoozeCancelled: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val alarmCollapsedIconAnimatedWidth by animateFloatAsState(targetValue =  if (alarmExpanded) 180f else 0f)

    Card(modifier = modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.extraLarge),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = alarmCardContainerColor
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onAddLabelClicked()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_label_24),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        text = label.ifEmpty { stringResource(id = R.string.add_label) }
                    )
                }
                Icon(
                    modifier = Modifier
                        .rotate(
                            alarmCollapsedIconAnimatedWidth
                        )
                        .clip(CircleShape)
                        .clickable {
                            onCollapseClicked()
                        }
                        .padding(4.dp),
                    painter = painterResource(id = R.drawable.baseline_expand_circle_down_24),
                    contentDescription = null
                )
            }
            AlarmTimeText(text = alarmTime, enabled = (alarmStatus == AlarmStatus.ON), onClick = onAlarmTimeClick)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = alarmScheduleText,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (alarmStatus == AlarmStatus.ON) Color.White else Color.Unspecified,
                    fontWeight = if (alarmStatus == AlarmStatus.ON) FontWeight.SemiBold else null
                ))
                Switch(checked = (alarmStatus == AlarmStatus.ON), onCheckedChange = {
                    onAlarmStatusChange(
                        when (alarmStatus) {
                            AlarmStatus.ON -> AlarmStatus.OFF
                            AlarmStatus.OFF -> AlarmStatus.ON
                        }
                    )
                })
            }
            /**
             *
             */
            AnimatedVisibility(visible = alarmExpanded) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    DaySelectorField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        weekDaySelectedText = alarmScheduleDays,
                        notifyWeekDaySelectionChange = { weekDay ->
                            onDaysSelected(weekDay)
                        })
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onScheduleAlarmClicked()
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                painter = painterResource(id = R.drawable.round_calendar_today_24),
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = stringResource(id = R.string.schedule_alarm_label),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(
                            painter = when(alarmScheduleType) {
                                AlarmScheduleType.ONCE -> painterResource(id = R.drawable.baseline_add_circle_outline_24)
                                AlarmScheduleType.SCHEDULE_ONCE -> painterResource(id = R.drawable.baseline_remove_circle_outline_24)
                                AlarmScheduleType.REPEATED -> painterResource(id = R.drawable.baseline_add_circle_outline_24)
                            },
                            contentDescription = null
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onVibrateStatusChange(!vibrateStatus)
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.padding(4.dp),
                                painter = painterResource(id = R.drawable.baseline_vibration_24),
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = stringResource(R.string.vibrate_label),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(
                            painter = painterResource(id = if (vibrateStatus) R.drawable.baseline_check_circle_24 else R.drawable.outline_circle_24 ),
                            contentDescription = null,
                            tint = if (vibrateStatus) weekDaySelectedColor else Color.Unspecified
                        )
                    }
                    if (isSnoozed) {
                        Text(
                            text = stringResource(
                                id = R.string.snoozed_until,
                                TimeUtils.getFormattedTime(TIME_FORMATS.HH_MM, alarmSnoozeMillis)
                            ),
                            modifier = Modifier.clickable { onSnoozeCancelled() },
                            style = MaterialTheme.typography.bodyMedium.copy(color = week_day_highlight_color)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onDeleteClick()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                        )
                        Text(
                            text = stringResource(R.string.delete_label),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }


        }
    }
}

@Preview
@Composable
fun AlarmCardPreview() {
    /**
     * index
     * label
     * onAddLabelClicked
     * onCollapseClicked
     * onAlarmStatusChange
     * onDaySelected
     * onScheduleAlarmClicked
     * onVibrateStatusChange
     * onDeleteClick
     */
    val expanded by remember {
        mutableStateOf(true)
    }
    AlarmCard(
        label = "Sample Label",
        alarmTime = "23:00",
        alarmStatus = AlarmStatus.ON,
        alarmScheduleText = "Everyday",
        alarmScheduleDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
        alarmScheduleType = AlarmScheduleType.SCHEDULE_ONCE,
        vibrateStatus = false,
        alarmExpanded = expanded,
        isSnoozed = true,
        alarmSnoozeMillis = System.currentTimeMillis(),
        onAddLabelClicked = { },
        onCollapseClicked = {
            expanded != expanded
        },
        onAlarmTimeClick = {},
        onAlarmStatusChange = {},
        onDaysSelected = {
        },
        onScheduleAlarmClicked = { },
        onVibrateStatusChange = {},
        onSnoozeCancelled = {},
        onDeleteClick = {}
    )
}