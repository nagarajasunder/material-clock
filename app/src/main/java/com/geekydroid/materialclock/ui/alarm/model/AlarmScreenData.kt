package com.geekydroid.materialclock.ui.alarm.model

/**
 * This class holds the data that are specific to Alarm screen actions
 * like showing the add label dialog or select time dialog
 */

data class AlarmScreenData(
    val showAddLabelDialog: Boolean,
    val labelValue: String,
    val showSelectTimeDialog: Boolean,
    val showTimeInput:Boolean,
    val alarmHour: Int,
    val alarmMinute: Int
) {

    companion object {
        val initialState = AlarmScreenData(
            showAddLabelDialog = false,
            labelValue = "",
            showSelectTimeDialog = false,
            showTimeInput = false,
            alarmHour = 0,
            alarmMinute = 0
        )
    }

}