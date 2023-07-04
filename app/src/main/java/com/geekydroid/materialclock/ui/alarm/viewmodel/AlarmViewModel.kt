package com.geekydroid.materialclock.ui.alarm.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.datastore.DatastoreKeyManager
import com.geekydroid.materialclock.application.datastore.DatastoreManager
import com.geekydroid.materialclock.application.utils.ResourceProvider
import com.geekydroid.materialclock.application.utils.TimeUtils
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.composables.WeekDay
import com.geekydroid.materialclock.ui.alarm.model.AlarmScreenData
import com.geekydroid.materialclock.ui.alarm.model.AlarmUiData
import com.geekydroid.materialclock.ui.alarm.screenactions.AlarmCardActions
import com.geekydroid.materialclock.ui.alarm.screenactions.AlarmScreenActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * SharingStarted.WhileSubscribed(5_000)
 * We are using this to stay subscribed even during configuration changes
 */

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val datastoreManager: DatastoreManager
) : ViewModel(), AlarmCardActions, AlarmScreenActions {

    //Todo("Find the best way to pass the data to UI")
    private val _alarms: MutableStateFlow<MutableList<AlarmUiData>> = MutableStateFlow(
        mutableStateListOf()
    )
    val alarms: StateFlow<List<AlarmUiData>> = _alarms.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = listOf()
    )

    private val _alarmScreenData: MutableStateFlow<AlarmScreenData> =
        MutableStateFlow(AlarmScreenData.initialState)
    val alarmScreenData: StateFlow<AlarmScreenData> = _alarmScreenData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AlarmScreenData.initialState
    )

    private var selectedAlarmIndexForLabelEdit = -1
    private var alarmTimeIndex = -1

    private var showTimeInput = false

    init {
        collectTimeInputUpdates()
    }

    private fun collectTimeInputUpdates() {
        viewModelScope.launch {
            datastoreManager.getBooleanValueOrNull(DatastoreKeyManager.SHOW_TIME_INPUT).collectLatest {newValue ->
                showTimeInput = newValue
            }
        }
    }

    override fun onAddAlarmClicked() {
        toggleAlarmTimeIndex(-1)
        _alarmScreenData.update {
            val alarmInitialTime = TimeUtils.getTimeOneHourFromNow()
            it.copy(
                showSelectTimeDialog = true,
                alarmHour = alarmInitialTime.first,
                alarmMinute = alarmInitialTime.second
            )
        }
    }

    private fun addNewAlarm(alarmHour: Int, alarmMinute: Int) {
        viewModelScope.launch {
            val newAlarm =
                AlarmUiData(
                    alarmLabel = "",
                    alarmTimeText = getAlarmTimeText(alarmHour, alarmMinute),
                    alarmHour = alarmHour,
                    alarmMinute = alarmMinute,
                    alarmStatus = AlarmStatus.ON,
                    alarmScheduledDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
                    alarmScheduleText = resourceProvider.getString(R.string.not_scheduled_label),
                    pauseAlarmText = "",
                    alarmSnoozeText = "",
                    showAlarmDismissCta = false,
                    isAlarmVibrate = false,
                    isAlarmExpanded = true
                )
            val newList = _alarms.value.toMutableList()
            newList.add(newAlarm)
            _alarms.value = newList
        }
    }

    override fun onAlarmTimeTextClicked(index: Int) {
        viewModelScope.launch {
            toggleAlarmTimeIndex(index)
            val selectedAlarm = _alarms.value[index]
            _alarmScreenData.update {
                it.copy(
                    showSelectTimeDialog = true,
                    alarmHour = selectedAlarm.alarmHour,
                    alarmMinute = selectedAlarm.alarmMinute,
                    showTimeInput = showTimeInput
                )
            }
        }
    }


    override fun onAlarmTimeChanged(calendar: Calendar,showTimeInput:Boolean) {
        viewModelScope.launch {
            datastoreManager.storeBooleanValue(DatastoreKeyManager.SHOW_TIME_INPUT,showTimeInput)
        }
        viewModelScope.launch {
            _alarmScreenData.update {
                it.copy(showSelectTimeDialog = false, alarmHour = 0, alarmMinute = 0)
            }
            if (alarmTimeIndex == -1) {
                addNewAlarm(
                    alarmHour = calendar.get(Calendar.HOUR_OF_DAY),
                    alarmMinute = calendar.get(Calendar.MINUTE)
                )
            }
            /**
             * To Avail index out of bounds exception
             */
            else if (alarmTimeIndex < _alarms.value.size) {
                var selectedAlarm = _alarms.value[alarmTimeIndex]
                selectedAlarm = selectedAlarm.copy(
                    alarmTimeText = getAlarmTimeText(
                        alarmHour = calendar.get(Calendar.HOUR_OF_DAY),
                        alarmMinute = calendar.get(Calendar.MINUTE)
                    )
                )
                updateAlarmsList(selectedAlarm, alarmTimeIndex)
            }
            toggleAlarmTimeIndex(-1)
        }

    }

    private fun getAlarmTimeText(alarmHour: Int, alarmMinute: Int): String {
        val alarmHourStr = if (alarmHour < 10) {
            "0${alarmHour}"
        } else {
            "$alarmHour"
        }
        val alarmMinuteStr = if (alarmMinute < 10) {
            "0${alarmMinute}"
        } else {
            "$alarmMinute"
        }
        return "${alarmHourStr}:${alarmMinuteStr}"
    }

    override fun onTimePickerDialogClosed() {
        viewModelScope.launch {
            toggleAlarmTimeIndex(-1)
            _alarmScreenData.update {
                it.copy(showSelectTimeDialog = false, alarmHour = 0, alarmMinute = 0)
            }
        }
    }

    override fun onAlarmScheduledDatesChanged() {

    }

    override fun onAlarmCollapsedChanged(index: Int) {
        viewModelScope.launch {
            var selectedAlarm = _alarms.value[index]
            selectedAlarm = selectedAlarm.copy(isAlarmExpanded = !selectedAlarm.isAlarmExpanded)
            updateAlarmsList(selectedAlarm, index)
        }
    }

    override fun onAddLabelClicked(index: Int) {
        viewModelScope.launch {
            _alarmScreenData.update {
                toggleSelectedLabelIndex(index)
                it.copy(showAddLabelDialog = true, labelValue = _alarms.value[index].alarmLabel)
            }
        }
    }

    override fun onAlarmLabelDialogDismissed() {
        viewModelScope.launch {
            _alarmScreenData.update {
                toggleSelectedLabelIndex(-1)
                it.copy(showAddLabelDialog = false)
            }
        }
    }

    override fun onLabelValueChange(newValue: String) {
        viewModelScope.launch {
            _alarmScreenData.update {
                it.copy(showAddLabelDialog = true, labelValue = newValue)
            }
        }
    }

    override fun onLabelValueConfirmed() {
        viewModelScope.launch {
            val selectedAlarm =
                _alarms.value[selectedAlarmIndexForLabelEdit].copy(alarmLabel = _alarmScreenData.value.labelValue)
            updateAlarmsList(selectedAlarm, selectedAlarmIndexForLabelEdit)
        }
        viewModelScope.launch {
            _alarmScreenData.update {
                it.copy(showAddLabelDialog = false, labelValue = "")
            }
        }

    }

    override fun onAlarmStatusChange(index: Int, newStatus: AlarmStatus) {
        viewModelScope.launch {
            var selectedAlarm = _alarms.value[index]
            selectedAlarm = selectedAlarm.copy(alarmStatus = newStatus)
            updateAlarmsList(selectedAlarm, index)
        }
    }

    override fun onAlarmScheduledDaysChange(
        index: Int,
        selectedDay: WeekDay
    ) {
        viewModelScope.launch {
            val dayIndex = selectedDay.index
            var selectedAlarm = _alarms.value[index]
            val dayChar = if (selectedAlarm.alarmScheduledDays[dayIndex].isUpperCase()) {
                selectedAlarm.alarmScheduledDays[dayIndex].lowercaseChar()
            } else {
                selectedAlarm.alarmScheduledDays[dayIndex].uppercaseChar()
            }
            var currentScheduleDays = selectedAlarm.alarmScheduledDays
            currentScheduleDays = "${
                currentScheduleDays.substring(
                    0,
                    dayIndex
                )
            }${dayChar}${currentScheduleDays.substring(dayIndex + 1)}"
            val alarmScheduleText = getScheduledTextFromDays(currentScheduleDays)

            selectedAlarm = selectedAlarm.copy(
                alarmScheduledDays = currentScheduleDays,
                alarmScheduleText = alarmScheduleText
            )
            updateAlarmsList(selectedAlarm, index)
        }
    }

    /**
     * This function will returns the scheduled days text from scheduled days
     * Eg. SmTwTfS => Sun,Tue,Thu,Sat
     */

    private fun getScheduledTextFromDays(scheduledDays: String): String {
        val weekDayList = WeekDay.values().asList()
        val scheduledDayList = mutableListOf<String>()
        scheduledDays.toCharArray().forEachIndexed { index, day ->
            if (day.isUpperCase()) {
                scheduledDayList.add(weekDayList[index].value)
            }
        }

        return when (scheduledDayList.size) {
            0 -> {
                resourceProvider.getString(R.string.not_scheduled_label)
            }
            /**
             * if all days are selected
             */
            weekDayList.size -> {
                Constants.EVERYDAY
            }

            else -> {
                scheduledDayList.joinToString(separator = ",")
            }
        }
    }


    private fun updateAlarmsList(selectedAlarm: AlarmUiData, index: Int) {
        _alarms.update {
            val newList = it.toMutableList()
            newList.removeAt(index)
            newList.add(index, selectedAlarm)
            newList
        }
    }

    override fun onScheduleAlarmClicked(index: Int) {

    }

    override fun onVibrationStatusChange(index: Int, newStatus: Boolean) {
        viewModelScope.launch {
            var selectedAlarm = _alarms.value[index]
            selectedAlarm = selectedAlarm.copy(isAlarmVibrate = newStatus)
            updateAlarmsList(selectedAlarm, index)
        }
    }

    override fun onDismissClicked(index: Int) {

    }

    override fun onDeleteClicked(index: Int) {
        toggleSelectedLabelIndex(-1)
        val newList = _alarms.value.toMutableList()
        newList.remove(_alarms.value[index])
        _alarms.value = newList
    }

    private fun toggleSelectedLabelIndex(index: Int) {
        selectedAlarmIndexForLabelEdit = index
    }

    private fun toggleAlarmTimeIndex(index: Int) {
        alarmTimeIndex = index
    }
}