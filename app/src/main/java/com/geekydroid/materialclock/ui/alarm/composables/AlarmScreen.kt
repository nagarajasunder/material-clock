package com.geekydroid.materialclock.ui.alarm.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geekydroid.materialclock.ui.alarm.model.AlarmScreenData
import com.geekydroid.materialclock.ui.alarm.viewmodel.AlarmViewModel

@Composable
fun AlarmScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AlarmViewModel = hiltViewModel()
) {

    val alarmData by viewModel.alarms.collectAsStateWithLifecycle(initialValue = listOf())
    val alarmScreenData by viewModel.alarmScreenData.collectAsStateWithLifecycle(AlarmScreenData.initialState)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                onClick = {
                    viewModel.onAddAlarmClicked()
                }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                if (alarmScreenData.showAddLabelDialog) {
                    AddLabelDialog(
                        value = alarmScreenData.labelValue,
                        onValueChange = viewModel::onLabelValueChange,
                        onLabelConfirmed = viewModel::onLabelValueConfirmed,
                        onDialogDismissed = viewModel::onAlarmLabelDialogDismissed
                    )
                } else if (alarmScreenData.showSelectTimeDialog) {
                    TimePickerDialogWrapper(
                        initialHour = alarmScreenData.alarmHour,
                        initialMinute = alarmScreenData.alarmMinute,
                        showPickerInitial = (!alarmScreenData.showTimeInput),
                        onConfirm = viewModel::onAlarmTimeChanged,
                        onCancel = viewModel::onTimePickerDialogClosed
                    )
                }
            }

            items(alarmData.size) { index ->
                val currentAlarmData = alarmData[index]
                AlarmCard(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    label = currentAlarmData.alarmLabel,
                    alarmTime = currentAlarmData.alarmTimeText,
                    alarmStatus = currentAlarmData.alarmStatus,
                    alarmScheduleText = currentAlarmData.alarmScheduleText,
                    alarmScheduleDays = currentAlarmData.alarmScheduledDays,
                    pauseAlarmText = currentAlarmData.pauseAlarmText,
                    vibrateStatus = currentAlarmData.isAlarmVibrate,
                    alarmExpanded = currentAlarmData.isAlarmExpanded,
                    onAddLabelClicked = {
                        viewModel.onAddLabelClicked(index)
                    },
                    onCollapseClicked = {
                        viewModel.onAlarmCollapsedChanged(index)
                    },
                    onAlarmTimeClick = {
                        viewModel.onAlarmTimeTextClicked(index)
                    },
                    onAlarmStatusChange = { newStatus ->
                        viewModel.onAlarmStatusChange(index, newStatus)
                    },
                    onDaysSelected = { selectedDay ->
                        viewModel.onAlarmScheduledDaysChange(index, selectedDay)
                    },
                    onScheduleAlarmClicked = {
                        viewModel.onScheduleAlarmClicked(index)
                    },
                    onVibrateStatusChange = { newStatus ->
                        viewModel.onVibrationStatusChange(index, newStatus)
                    },
                    onDeleteClick = {
                        viewModel.onDeleteClicked(index)
                    }
                )
            }
        }
    }

}