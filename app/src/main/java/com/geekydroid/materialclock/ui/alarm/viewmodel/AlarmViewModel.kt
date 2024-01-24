package com.geekydroid.materialclock.ui.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.datastore.DatastoreKeyManager
import com.geekydroid.materialclock.application.datastore.DatastoreManager
import com.geekydroid.materialclock.application.datastore.DatastoreManagerImpl
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.application.utils.AlarmUtils
import com.geekydroid.materialclock.application.utils.ResourceProvider
import com.geekydroid.materialclock.application.utils.TIME_FORMATS
import com.geekydroid.materialclock.application.utils.TimeUtils
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.composables.WeekDay
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import com.geekydroid.materialclock.ui.alarm.model.AlarmScreenData
import com.geekydroid.materialclock.ui.alarm.model.AlarmUiData
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import com.geekydroid.materialclock.ui.alarm.screenactions.AlarmCardActions
import com.geekydroid.materialclock.ui.alarm.screenactions.AlarmScreenActions
import com.geekydroid.materialclock.ui.alarm.screenevents.AlarmScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

private const val TAG = "AlarmViewModel"

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AlarmViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val datastoreManager: DatastoreManager,
    private val alarmRepository: AlarmRepository,
    @IoDispatcher private val externalDispatcher: CoroutineDispatcher
) : ViewModel(), AlarmCardActions, AlarmScreenActions {


    val alarmUiDataList: StateFlow<List<AlarmUiData>> =
        alarmRepository.getAllAlarms()
            .mapLatest {
                transformAlarmMasterToAlarmUiData(it)
            }
            .flowOn(externalDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
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

    private val eventsChannel = Channel<AlarmScreenEvents>()
    val screenEvents : Flow<AlarmScreenEvents> = eventsChannel.receiveAsFlow()

    init {
        collectTimeInputUpdates()
    }

    private fun transformAlarmMasterToAlarmUiData(alarmMasterList: List<AlarmMaster>): List<AlarmUiData> {
        val alarmUiDataList = mutableListOf<AlarmUiData>()
        alarmMasterList.forEachIndexed { _, alarmMaster ->
            val alarmUiData = AlarmUiData(
                alarmId = alarmMaster.alarmId,
                alarmLabel = alarmMaster.alarmLabel,
                alarmTimeText = getAlarmTimeText(alarmMaster.alarmTimeInMillis),
                alarmTimeInMills = alarmMaster.alarmTimeInMillis,
                alarmDateInMillis = alarmMaster.alarmDateInMillis,
                alarmStatus = alarmMaster.alarmStatus,
                alarmScheduleType = alarmMaster.alarmType,
                alarmScheduledDays = alarmMaster.alarmScheduledDays,
                alarmScheduleText = getAlarmScheduleText(
                    alarmScheduleType = alarmMaster.alarmType,
                    timeInMillis = alarmMaster.alarmTimeInMillis,
                    alarmScheduleDays = alarmMaster.alarmScheduledDays,
                    alarmDateMillis = alarmMaster.alarmDateInMillis,
                    alarmStatus = alarmMaster.alarmStatus,
                    isSnoozed = alarmMaster.isSnoozed
                ),
                isAlarmVibrate = alarmMaster.isAlarmVibrate,
                isAlarmSnooze = alarmMaster.isSnoozed,
                alarmSoundIndex = alarmMaster.alarmSoundIndex,
                alarmSnoozeMillis = alarmMaster.snoozeMillis
            )
            alarmUiDataList.add(alarmUiData)

        }
        return alarmUiDataList
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
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        viewModelScope.launch {
            val newAlarm =
                AlarmUiData(
                    alarmId = 0,
                    alarmLabel = "",
                    alarmTimeText = getAlarmTimeText(calendar.timeInMillis),
                    alarmTimeInMills = calendar.timeInMillis,
                    alarmDateInMillis = calendar.timeInMillis,
                    alarmStatus = AlarmStatus.ON,
                    alarmScheduledDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR,
                    alarmScheduleType = AlarmScheduleType.ONCE,
                    alarmSnoozeText = "",
                    showAlarmDismissCta = false,
                    alarmSoundIndex = 0,
                    isAlarmVibrate = false,
                )
            insertNewAlarm(newAlarm)
        }
    }

    private suspend fun insertNewAlarm(newAlarm: AlarmUiData) {
        val alarmMaster: AlarmMaster = transformAlarmUiDataToAlarmMaster(newAlarm)
        val alarmId = alarmRepository.insertNewAlarm(alarmMaster)
        eventsChannel.send(AlarmScreenEvents.ScheduleAlarm(alarmMaster.copy(alarmId = alarmId.toInt())))
        eventsChannel.send(AlarmScreenEvents.ShowToast(AlarmUtils.getAlarmTimeDifferenceText(alarmMaster.alarmTriggerMillis)))
    }

    override fun onAlarmTimeTextClicked(index: Int) {
        viewModelScope.launch {
            toggleAlarmTimeIndex(index)
            val selectedAlarm = alarmUiDataList.value[index]
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
            calendar.set(Calendar.SECOND,0)
            calendar.set(Calendar.MILLISECOND,0)
            if (TimeUtils.isPastTime(calendar.timeInMillis)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            if (alarmTimeIndex == -1) {
                addNewAlarm(calendar = calendar)
            }
            /**
             * To Avail index out of bounds exception
             */
            else if (alarmTimeIndex < alarmUiDataList.value.size) {
                var selectedAlarm = alarmUiDataList.value[alarmTimeIndex]
                selectedAlarm = selectedAlarm.copy(
                    alarmTimeText = getAlarmTimeText(calendar.timeInMillis),
                    alarmTimeInMills = calendar.timeInMillis
                )
                updateAlarmsList(selectedAlarm, rescheduleAlarm = true)
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
        viewModelScope.launch {
            val selectedAlarm = alarmUiDataList.value[selectedAlarmIndex]
            updateAlarmsList(
                selectedAlarm = selectedAlarm.copy(
                    alarmScheduleType = AlarmScheduleType.SCHEDULE_ONCE,
                    alarmDateInMillis = time,
                    alarmScheduledDays = Constants.WEEK_DAYS_UNSELECTED_DEFAULT_STR
                ),
                rescheduleAlarm = (selectedAlarm.alarmStatus == AlarmStatus.ON)
            )
            toggleSelectedAlarmIndex(-1)
            _alarmScreenData.update {
                it.copy(showDatePicker = false)
            }
        }
    }

    override fun onAlarmCollapsedChanged(index: Int) {
        viewModelScope.launch {
            val expandedAlarmIndex: Int = if (_alarmScreenData.value.expandedAlarmIndex == index) {
                -1
            } else {
                index
            }
            _alarmScreenData.update {
                it.copy(expandedAlarmIndex = expandedAlarmIndex)
            }
        }
    }

    override fun onAddLabelClicked(index: Int) {
        viewModelScope.launch {
            _alarmScreenData.update {
                toggleSelectedAlarmIndex(index)
                it.copy(
                    showAddLabelDialog = true,
                    labelValue = alarmUiDataList.value[index].alarmLabel
                )
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
                alarmUiDataList.value[selectedAlarmIndex].copy(alarmLabel = _alarmScreenData.value.labelValue)
            updateAlarmsList(selectedAlarm, rescheduleAlarm = (selectedAlarm.alarmStatus == AlarmStatus.ON))
        }
        viewModelScope.launch {
            _alarmScreenData.update {
                it.copy(showAddLabelDialog = false, labelValue = "")
            }
        }

    }

    override fun onAlarmStatusChange(index: Int, newStatus: AlarmStatus) {
        viewModelScope.launch {
            var selectedAlarm = alarmUiDataList.value[index]
            selectedAlarm = selectedAlarm.copy(
                    alarmStatus = newStatus,
                    alarmScheduleText = getAlarmScheduleText(
                        alarmScheduleType = selectedAlarm.alarmScheduleType,
                        timeInMillis = selectedAlarm.alarmTimeInMills,
                        alarmScheduleDays = selectedAlarm.alarmScheduledDays,
                        alarmDateMillis = selectedAlarm.alarmDateInMillis,
                        alarmStatus = newStatus,
                        isSnoozed = selectedAlarm.isAlarmSnooze
                    )
                )
            when(newStatus) {
                AlarmStatus.ON -> {
                    updateAlarmsList(selectedAlarm, rescheduleAlarm = true)
                }
                AlarmStatus.OFF -> {
                    updateAlarmsList(selectedAlarm, rescheduleAlarm = false)
                    eventsChannel.send(AlarmScreenEvents.CancelAlarm(alarmId = selectedAlarm.alarmId))
                }
            }

        }
    }

    override fun onAlarmScheduledDaysChange(
        index: Int,
        selectedDay: WeekDay
    ) {
        viewModelScope.launch {
            val dayIndex = selectedDay.index
            var selectedAlarm = alarmUiDataList.value[index]
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
            selectedAlarm = selectedAlarm.copy(
                alarmScheduledDays = currentScheduleDays,
                alarmScheduleType = alarmScheduleType
            )
            eventsChannel.send(AlarmScreenEvents.CancelAlarm(selectedAlarm.alarmId))
            updateAlarmsList(selectedAlarm, rescheduleAlarm = (selectedAlarm.alarmStatus == AlarmStatus.ON))
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
        alarmDateMillis: Long,
        alarmStatus:AlarmStatus,
        isSnoozed:Boolean
    ): String {

        val alarmTriggerMillis = AlarmUtils.getAlarmTimeBasedOnConstraints(
            alarmScheduleType = alarmScheduleType,
            alarmScheduleDays = alarmScheduleDays,
            alarmTimeMillis = timeInMillis,
            alarmDateMillis = alarmDateMillis,
            /**
             * We are passing false because we need to keep the Alarm schedule text as `Today` even if the time is passed. This scenario happens when the user snoozes the alarm
             */
            addNextDayIfTimeIsPast = !isSnoozed
        )

        return when (alarmScheduleType) {
            AlarmScheduleType.ONCE -> {
                when(alarmStatus) {
                    AlarmStatus.ON -> {

                        if (TimeUtils.isTomorrow(alarmTriggerMillis)) resourceProvider.getString(R.string.tomorrow) else resourceProvider.getString(
                            R.string.today
                        )
                    }
                    AlarmStatus.OFF -> {
                       resourceProvider.getString(R.string.not_scheduled_label)
                    }
                }

            }

            AlarmScheduleType.SCHEDULE_ONCE -> {
                val scheduleString = TimeUtils.getFormattedTime(TIME_FORMATS.MMM_DD_YYYY, alarmDateMillis).replace(", ${TimeUtils.getFormattedTime(TIME_FORMATS.YYYY,System.currentTimeMillis())}","",true)
                resourceProvider.getString(
                    R.string.scheduled_for,
                    scheduleString
                )
            }

            AlarmScheduleType.REPEATED -> {
                getScheduledTextFromDays(alarmScheduleDays)
            }
        }
    }


    private suspend fun updateAlarmsList(selectedAlarm: AlarmUiData, updateDb: Boolean = true,rescheduleAlarm:Boolean) {
        if (updateDb) {
            val alarmScheduleMillis = AlarmUtils.getAlarmTimeBasedOnConstraints(
                alarmScheduleType = selectedAlarm.alarmScheduleType,
                alarmScheduleDays = selectedAlarm.alarmScheduledDays,
                alarmTimeMillis = selectedAlarm.alarmTimeInMills,
                alarmDateMillis = selectedAlarm.alarmDateInMillis
            )
            val alarmMaster: AlarmMaster =
                transformAlarmUiDataToAlarmMaster(selectedAlarm).copy(alarmTriggerMillis = alarmScheduleMillis)
            alarmRepository.updateExistingAlarm(alarmMaster)
            if (rescheduleAlarm && alarmMaster.alarmStatus == AlarmStatus.ON) {
                eventsChannel.send(AlarmScreenEvents.ScheduleAlarm(alarmMaster))
                eventsChannel.send(AlarmScreenEvents.ShowToast(AlarmUtils.getAlarmTimeDifferenceText(alarmMaster.alarmTriggerMillis)))
            }
        }
    }

    private fun transformAlarmUiDataToAlarmMaster(alarmUiData: AlarmUiData): AlarmMaster {
        return AlarmMaster(
            alarmId = alarmUiData.alarmId,
            alarmStatus = alarmUiData.alarmStatus,
            alarmLabel = alarmUiData.alarmLabel,
            alarmTimeInMillis = alarmUiData.alarmTimeInMills,
            alarmDateInMillis = alarmUiData.alarmDateInMillis,
            alarmTriggerMillis = alarmUiData.alarmTimeInMills,
            alarmScheduledDays = alarmUiData.alarmScheduledDays,
            alarmType = alarmUiData.alarmScheduleType,
            alarmSoundIndex = alarmUiData.alarmSoundIndex,
            isAlarmVibrate = alarmUiData.isAlarmVibrate,
            createdOn = System.currentTimeMillis(),
            updatedOn = System.currentTimeMillis()
        )
    }

    override fun onScheduleAlarmClicked(index: Int) {
        viewModelScope.launch {
            val selectedAlarm = alarmUiDataList.value[index]
            toggleSelectedAlarmIndex(index)
            _alarmScreenData.update {
                showDatePicker(selectedAlarm, it)
            }
        }
    }

    override fun onScheduleAlarmCancelled(index:Int) {
        viewModelScope.launch {
            val selectedAlarm = alarmUiDataList.value[index]
            updateAlarmsList(
                selectedAlarm = selectedAlarm.copy(
                    alarmScheduleType = AlarmScheduleType.ONCE,
                    alarmDateInMillis = selectedAlarm.alarmTimeInMills
                ),
                rescheduleAlarm = (selectedAlarm.alarmStatus == AlarmStatus.ON)
            )
        }
    }

    override fun onAlarmSoundChange(index: Int) {
        viewModelScope.launch {
            val selectedAlarm = alarmUiDataList.value[index]
            eventsChannel.send(AlarmScreenEvents.OpenAlarmSoundScreen(selectedAlarm.alarmId))
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
            var selectedAlarm = alarmUiDataList.value[index]
            selectedAlarm = selectedAlarm.copy(isAlarmVibrate = newStatus)
            updateAlarmsList(selectedAlarm, rescheduleAlarm = (selectedAlarm.alarmStatus == AlarmStatus.ON))
        }
    }

    override fun onSnoozeCancelled(index: Int) {
        toggleSelectedAlarmIndex(-1)
        val selectedAlarm = alarmUiDataList.value[index]
        deleteSnoozedAlarm(selectedAlarm.alarmId,selectedAlarm.alarmScheduleType)
    }

    override fun onDeleteClicked(index: Int) {
        toggleSelectedAlarmIndex(-1)
        val selectedAlarm = alarmUiDataList.value[index]
        deleteAlarm(selectedAlarm.alarmId)
    }

    private fun deleteAlarm(alarmId: Int) {
        viewModelScope.launch {
            alarmRepository.deleteAlarmById(alarmId)
            eventsChannel.send(AlarmScreenEvents.CancelAlarm(alarmId))
        }
    }

    private fun deleteSnoozedAlarm(alarmId: Int,alarmScheduleType: AlarmScheduleType) {
        viewModelScope.launch {
            val alarmStatus = when(alarmScheduleType) {
                AlarmScheduleType.ONCE,AlarmScheduleType.SCHEDULE_ONCE -> AlarmStatus.OFF
                AlarmScheduleType.REPEATED -> AlarmStatus.ON
            }
            alarmRepository.updateSnoozeDetails(
                alarmId = alarmId,
                alarmStatus = alarmStatus,
                isSnoozed = false,
                snoozeMillis = 0L
            )
            eventsChannel.send(AlarmScreenEvents.CancelSnoozedAlarm(alarmId))
        }
    }

    private fun toggleSelectedAlarmIndex(index: Int) {
        selectedAlarmIndex = index
    }

    private fun toggleAlarmTimeIndex(index: Int) {
        alarmTimeIndex = index
    }
}