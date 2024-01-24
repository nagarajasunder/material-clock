package com.geekydroid.materialclock.ui.alarm.viewmodel

import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepositoryImpl
import com.geekydroid.materialclock.ui.alarm.screenactions.AlarmSoundScreenActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSoundViewModel @Inject constructor(
    private val repository: AlarmRepositoryImpl
) : ViewModel(), AlarmSoundScreenActions {

    private var alarmId = 0
    private var _currentAlarmSoundIdx = MutableStateFlow(0)
    val currentAlarmSoundIdx:StateFlow<Int> = _currentAlarmSoundIdx

    fun setAlarmId(id:Int) {
        alarmId = id
        viewModelScope.launch {
            _currentAlarmSoundIdx.value = repository.getAlarmSoundId(alarmId).first()
        }
    }

    override fun updateAlarmSoundId(@IntRange(0,1) soundId: Int) {
        viewModelScope.launch {
            repository.updateAlarmSoundId(alarmId, soundId)
            _currentAlarmSoundIdx.value = soundId
        }
    }


}