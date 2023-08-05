package com.geekydroid.materialclock.ui.alarm.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.datastore.DatastoreKeyManager
import com.geekydroid.materialclock.application.datastore.DatastoreManager
import com.geekydroid.materialclock.application.utils.ResourceProvider
import com.geekydroid.materialclock.application.utils.TIME_FORMATS
import com.geekydroid.materialclock.application.utils.TimeUtils
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
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

    private var selectedAlarmIndex = -1
    private var alarmTimeIndex = -1
    private var showTimeInput = false

    init {
        collectTimeInputUpdates()
    }

    private fun collectTimeInputUpdates() {
        viewModelScope.launch {
            datastoreManager.getBooleanValueOrNull(DatastoreKeyManager.SHOW_TIME_INPUT)
                .collectLatest { newValue ->
                    showTimeInput = newValue
                }
        }
    }

    override fun onAddAlarmClicked() {
        toggleAlarmTimeIndex(-1)
        _alarmScreenData.update {
            it.copy(
                showSelectTimeDialog = true,
                alarmTimeInMillis = TimeUtils.getOneHourFromNowInMillis()
            )
        }
    }

    private fun addNewAlarm(calendar: Calendar) {
        viewModelScope.launch {
            val newAlarm =
                AlarmUiData(
                    alarmLabel = "",
                    alarmTimeText = getAlarmTimeText(calendar.timeInMillis),
                    alarmTimeInMills = calendar.timeInMillis,
                    alarmDateInMillis = calendar.timeInMillis,
                    alarmStatus = AlarmStatus.ON,
                    alarmScheduledDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
                    alarmScheduleType = AlarmScheduleType.ONCE,
                    alarmScheduleText = getAlarmScheduleText(
                        AlarmScheduleType.ONCE,
                        calendar.timeInMillis,
                        alarmScheduleDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
                        alarmDateMillis = calendar.timeInMillis
                    ),
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
                    alarmTimeInMillis = selectedAlarm.alarmTimeInMills,
                    showTimeInput = showTimeInput
                )
            }
        }
    }


    override fun onAlarmTimeChanged(calendar: Calendar, showTimeInput: Boolean) {
        viewModelScope.launch {
            datastoreManager.storeBooleanValue(DatastoreKeyManager.SHOW_TIME_INPUT, showTimeInput)
        }
        viewModelScope.launch {
            _alarmScreenData.update {
                it.copy(showSelectTimeDialog = false, alarmTimeInMillis = 0L)
            }
            if (TimeUtils.isPastTime(calendar.timeInMillis)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            if (alarmTimeIndex == -1) {
                addNewAlarm(calendar = calendar)
            }
            /**
             * To Avail index out of bounds exception
             */
            else if (alarmTimeIndex < _alarms.value.size) {
                var selectedAlarm = _alarms.value[alarmTimeIndex]
                selectedAlarm = selectedAlarm.copy(
                    alarmTimeText = getAlarmTimeText(calendar.timeInMillis),
                    alarmTimeInMills = calendar.timeInMillis,
                    alarmDateInMillis = calendar.timeInMillis,
                    alarmScheduleText = getAlarmScheduleText(
                        alarmScheduleType = selectedAlarm.alarmScheduleType,
                        timeInMillis = calendar.timeInMillis,
                        alarmScheduleDays = selectedAlarm.alarmScheduledDays,
                        alarmDateMillis = calendar.timeInMillis
                    )
                )
                updateAlarmsList(selectedAlarm, alarmTimeIndex)
            }
            toggleAlarmTimeIndex(-1)
        }

    }

    private fun getAlarmTimeText(timeInMillis: Long): String {
        val hourMinutePair = TimeUtils.getHourMinuteFromMillis(timeInMillis)
        val alarmHour = hourMinutePair.first
        val alarmMinute = hourMinutePair.second
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
                it.copy(showSelectTimeDialog = false, alarmTimeInMillis = 0L)
            }
        }
    }

    override fun onDatePickerDialogDismissed() {
        viewModelScope.launch {
            toggleSelectedAlarmIndex(-1)
            _alarmScreenData.update {
                it.copy(showDatePicker = false)
            }
        }
    }

    override fun onDatePickerConfirmed(time: Long) {
        //Todo("Close the Time picker and update the alarm time in millis")
        viewModelScope.launch {
            val selectedAlarm = _alarms.value[selectedAlarmIndex]
            val alarmScheduleText = getAlarmScheduleText(
                alarmScheduleType = AlarmScheduleType.SCHEDULE_ONCE,
                timeInMillis = selectedAlarm.alarmTimeInMills,
                alarmScheduleDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
                alarmDateMillis = time
            )
            updateAlarmsList(
                selectedAlarm = selectedAlarm.copy(
                    alarmScheduleType = AlarmScheduleType.SCHEDULE_ONCE,
                    alarmDateInMillis = time,
                    alarmScheduleText = alarmScheduleText,
                    alarmScheduledDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR
                ), index = selectedAlarmIndex
            )
            toggleSelectedAlarmIndex(-1)
            _alarmScreenData.update {
                it.copy(showDatePicker = false)
            }
        }
    }

    override fun onDateRangePickerConfirmed(timePair: Pair<Long, Long>) {

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
                toggleSelectedAlarmIndex(index)
                it.copy(showAddLabelDialog = true, labelValue = _alarms.value[index].alarmLabel)
            }
        }
    }

    override fun onAlarmLabelDialogDismissed() {
        viewModelScope.launch {
            _alarmScreenData.update {
                toggleSelectedAlarmIndex(-1)
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
                _alarms.value[selectedAlarmIndex].copy(alarmLabel = _alarmScreenData.value.labelValue)
            updateAlarmsList(selectedAlarm, selectedAlarmIndex)
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
            selectedAlarm =
                selectedAlarm.copy(alarmStatus = newStatus)
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
            var currentScheduleDays: String = selectedAlarm.alarmScheduledDays
            currentScheduleDays = "${
                currentScheduleDays.substring(
                    0,
                    dayIndex
                )
            }${dayChar}${currentScheduleDays.substring(dayIndex + 1)}"
            val alarmScheduleType =
                if (currentScheduleDays == Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR) AlarmScheduleType.ONCE else AlarmScheduleType.REPEATED
            val alarmScheduleText = getAlarmScheduleText(
                alarmScheduleType = alarmScheduleType,
                timeInMillis = selectedAlarm.alarmTimeInMills,
                alarmScheduleDays = currentScheduleDays,
                alarmDateMillis = selectedAlarm.alarmDateInMillis
            )

            selectedAlarm = selectedAlarm.copy(
                alarmScheduledDays = currentScheduleDays,
                alarmScheduleText = alarmScheduleText,
                alarmScheduleType = alarmScheduleType
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

    private fun getAlarmScheduleText(
        alarmScheduleType: AlarmScheduleType,
        timeInMillis: Long,
        alarmScheduleDays: String,
        alarmDateMillis: Long
    ): String {
        return when (alarmScheduleType) {
            AlarmScheduleType.ONCE -> {
                if (TimeUtils.isTomorrow(timeInMillis)) resourceProvider.getString(R.string.tomorrow) else resourceProvider.getString(
                    R.string.today
                )
            }

            AlarmScheduleType.SCHEDULE_ONCE -> {
                resourceProvider.getString(
                    R.string.scheduled_for,
                    TimeUtils.getFormattedTime(TIME_FORMATS.MMM_DD, alarmDateMillis)
                )
            }

            AlarmScheduleType.REPEATED -> {
                getScheduledTextFromDays(alarmScheduleDays)
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
        viewModelScope.launch {
            val selectedAlarm = _alarms.value[index]
            when (selectedAlarm.alarmScheduleType) {
                AlarmScheduleType.ONCE, AlarmScheduleType.REPEATED -> {
                    toggleSelectedAlarmIndex(index)
                    _alarmScreenData.update {
                        showDatePicker(selectedAlarm, it)
                    }
                }

                AlarmScheduleType.SCHEDULE_ONCE -> {
                    updateAlarmsList(
                        selectedAlarm = selectedAlarm.copy(
                            alarmScheduleType = AlarmScheduleType.ONCE,
                            alarmDateInMillis = selectedAlarm.alarmTimeInMills,
                            alarmScheduleText = getAlarmScheduleText(
                                alarmScheduleType = AlarmScheduleType.ONCE,
                                timeInMillis = selectedAlarm.alarmDateInMillis,
                                alarmScheduleDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
                                alarmDateMillis = selectedAlarm.alarmDateInMillis
                            )
                        ),
                        index = index
                    )
                }

            }
        }
    }

    private fun showDatePicker(
        selectedAlarm: AlarmUiData,
        alarmScreenData: AlarmScreenData
    ): AlarmScreenData {
        val timeText =
            TimeUtils.getFormattedTime(TIME_FORMATS.HH_MM, selectedAlarm.alarmTimeInMills)
        val title = resourceProvider.getString(R.string.alarm_date_picker_title, timeText)
        return alarmScreenData.copy(showDatePicker = true, datePickerTitle = title)
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
        toggleSelectedAlarmIndex(-1)
        val newList = _alarms.value.toMutableList()
        newList.remove(_alarms.value[index])
        _alarms.value = newList
    }

    private fun toggleSelectedAlarmIndex(index: Int) {
        selectedAlarmIndex = index
    }

    private fun toggleAlarmTimeIndex(index: Int) {
        alarmTimeIndex = index
    }

}