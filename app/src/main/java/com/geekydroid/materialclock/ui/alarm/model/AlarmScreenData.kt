package com.geekydroid.materialclock.ui.alarm.model

/**
 * This class holds the data that are specific to Alarm screen actions
 * like showing the add label dialog or select time dialog
 */

data class AlarmScreenData(
    val expandedAlarmIndex: Int = -1,
    val showAddLabelDialog: Boolean,
    val labelValue: String,
    val showSelectTimeDialog: Boolean,
    val showTimeInput: Boolean,
    val alarmTimeInMillis: Long,
    val showDatePicker: Boolean,
    val datePickerTitle: String,
    val showDateRangePicker: Boolean
) {

    companion object {
        val initialState = AlarmScreenData(
            showAddLabelDialog = false,
            labelValue = "",
            showSelectTimeDialog = false,
            showTimeInput = false,
            alarmTimeInMillis = 0L,
            showDatePicker = false,
            datePickerTitle = "",
            showDateRangePicker = false
        )
    }

}