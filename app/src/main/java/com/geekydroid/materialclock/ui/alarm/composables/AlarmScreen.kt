package com.geekydroid.materialclock.ui.alarm.composables

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.utils.AlarmScheduler
import com.geekydroid.materialclock.ui.alarm.model.AlarmScreenData
import com.geekydroid.materialclock.ui.alarm.screenevents.AlarmScreenEvents
import com.geekydroid.materialclock.ui.alarm.viewmodel.AlarmViewModel


@Composable
fun AlarmScreenContent(
    modifier: Modifier = Modifier,
    viewModel: AlarmViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {

    val context: Context = LocalContext.current
    val alarmData by viewModel.alarmUiDataList.collectAsStateWithLifecycle(initialValue = listOf())
    val alarmScreenData by viewModel.alarmScreenData.collectAsStateWithLifecycle(AlarmScreenData.initialState)
    var showNotificationPermissionDialog by remember {
        mutableStateOf(false)
    }
    val activityResulLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {permissionResult ->
        if (!permissionResult) {
            Toast.makeText(context,
                context.getString(R.string.notification_permission_denied_message),Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!isNotificationPermissionGiven(context)) {
            showNotificationPermissionDialog = true
        }
        viewModel.screenEvents.collect { event ->
            when (event) {
                is AlarmScreenEvents.ScheduleAlarm -> {
                    val alarmMaster = event.alarmMaster
                    AlarmScheduler.scheduleAlarmWithReminder(
                        context = context,
                        alarmId = alarmMaster.alarmId,
                        alarmDateMillis = alarmMaster.alarmDateInMillis,
                        alarmTimeMillis = alarmMaster.alarmTimeInMillis,
                        alarmTriggerMillis = alarmMaster.alarmTriggerMillis,
                        alarmLabel = alarmMaster.alarmLabel,
                        alarmScheduleType = alarmMaster.alarmType,
                        alarmScheduleDays = alarmMaster.alarmScheduledDays,
                        isAlarmVibrate = alarmMaster.isAlarmVibrate,
                    )
                }

                is AlarmScreenEvents.CancelAlarm -> {
                    AlarmScheduler.cancelAlarm(context, event.alarmId)
                }

                is AlarmScreenEvents.CancelSnoozedAlarm -> {
                    AlarmScheduler.cancelSnoozedAlarm(context, event.alarmId)
                }

                is AlarmScreenEvents.OpenAlarmSoundScreen -> {
                    navHostController.navigate(buildAlarmSoundScreenRoute(event.alarmId, true))
                }

                is AlarmScreenEvents.ShowToast -> {
                    Toast.makeText(context,event.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                onClick = {
                    viewModel.onAddAlarmClicked()
                }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_alarm)
                )
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
                        initialTimeMillis = alarmScreenData.alarmTimeInMillis,
                        showPickerInitial = (!alarmScreenData.showTimeInput),
                        onConfirm = viewModel::onAlarmTimeChanged,
                        onCancel = viewModel::onTimePickerDialogClosed
                    )
                } else if (alarmScreenData.showDatePicker) {
                    DatePickerDialogWrapper(
                        titleText = alarmScreenData.datePickerTitle,
                        onDismissed = viewModel::onDatePickerDialogDismissed,
                        onConfirmed = viewModel::onDatePickerConfirmed
                    )
                }
                else if (showNotificationPermissionDialog) {
                    OkButtonDialog(
                        title = context.getString(R.string.notification_permission_title),
                        message = context.getString(R.string.notification_permission_text)
                    ) {
                        showNotificationPermissionDialog = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            activityResulLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
                else if (alarmData.isEmpty()) {
                    Column(
                        Modifier.fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.size(100.dp),
                                painter = painterResource(id = R.drawable.baseline_no_alarm_24),
                                contentDescription = stringResource(
                                    id = R.string.no_alarm
                                ),
                                colorFilter = ColorFilter.tint(color = Color.DarkGray)
                            )
                            Image(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(
                                        top = 32.dp,
                                        bottom = 26.dp,
                                        start = 26.dp,
                                        end = 26.dp
                                    ),
                                painter = painterResource(id = R.drawable.close_24px),
                                contentDescription = stringResource(
                                    id = R.string.no_alarm
                                ),
                                colorFilter = ColorFilter.tint(color = Color.DarkGray)
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.no_alarm),
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.DarkGray)
                        )
                    }
                }
            }

            items(alarmData.size, key = { index ->
                alarmData[index].alarmId
            }) { index ->
                val currentAlarmData = alarmData[index]
                AlarmCard(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    label = currentAlarmData.alarmLabel,
                    alarmTime = currentAlarmData.alarmTimeText,
                    alarmStatus = currentAlarmData.alarmStatus,
                    alarmScheduleText = currentAlarmData.alarmScheduleText,
                    alarmScheduleDays = currentAlarmData.alarmScheduledDays,
                    alarmScheduleType = currentAlarmData.alarmScheduleType,
                    vibrateStatus = currentAlarmData.isAlarmVibrate,
                    alarmExpanded = alarmScreenData.expandedAlarmIndex == index,
                    isSnoozed = currentAlarmData.isAlarmSnooze,
                    alarmSoundIndex = currentAlarmData.alarmSoundIndex,
                    alarmSnoozeMillis = currentAlarmData.alarmSnoozeMillis,
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
                    onScheduleAlarmCancelled = {
                        viewModel.onScheduleAlarmCancelled(index)
                    },
                    onAlarmSoundChange = {
                        viewModel.onAlarmSoundChange(index)
                    },
                    onVibrateStatusChange = { newStatus ->
                        viewModel.onVibrationStatusChange(index, newStatus)
                    },
                    onSnoozeCancelled = {
                        viewModel.onSnoozeCancelled(index)
                    },
                    onDeleteClick = {
                        viewModel.onDeleteClicked(index)
                    }
                )
            }
        }
    }

}

fun isNotificationPermissionGiven(context: Context) : Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
    return true
}
