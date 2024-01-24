package com.geekydroid.materialclock.shared_test.ui.data.mock

import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object MockData {


    private val alarmList: MutableList<AlarmMaster> = mutableListOf()

    private val alarmFlow = MutableStateFlow(alarmList)

    suspend fun addAlarm(newAlarm:AlarmMaster) {
        alarmList.add(newAlarm)
        alarmFlow.emit(alarmList)
    }

    fun getAlarms() : Flow<List<AlarmMaster>> = alarmFlow
    fun deleteAlarm(index:Int) {
        alarmList.removeAt(index-1)
    }

}