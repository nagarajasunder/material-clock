package com.geekydroid.materialclock.ui.alarm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class AlarmFullScreenViewModel @Inject constructor(
    @ApplicationScope private val externalScope:CoroutineScope
) : ViewModel() {

    private val _alarmData:MutableLiveData<AlarmMaster?> = MutableLiveData(null)
    val alarmData:LiveData<AlarmMaster?> = _alarmData
    val timeFlow = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(1000)
        }
    }.shareIn(externalScope, started = SharingStarted.WhileSubscribed(5000))

    fun updateAlarmData(alarmMaster: AlarmMaster) {
        _alarmData.value = alarmMaster
    }

    fun onSnoozeClick() {

    }

    fun onDismissClick() {

    }
}