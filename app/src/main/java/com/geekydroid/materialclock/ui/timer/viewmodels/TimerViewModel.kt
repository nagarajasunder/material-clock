package com.geekydroid.materialclock.ui.timer.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.application.utils.TimerUtils
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState
import com.geekydroid.materialclock.ui.timer.screenactions.TimerScreenActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TimerViewModel"

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel(), TimerScreenActions {

    private val _timerScreenState = MutableStateFlow(TimerScreenState.initialState)
    val timerScreenState: StateFlow<TimerScreenState> = _timerScreenState

    override fun onTimerInputChanged(input: String) {
        Log.d(TAG, "onTimerInputChanged: ${viewModelScope.coroutineContext}")
        viewModelScope.launch {
            _timerScreenState.update {
                val newTimerText = if (input == "X") {
                    TimerUtils.getTimerTextAfterDeletion(
                        hour = it.timerHr,
                        minute = it.timerMin,
                        second = it.timerSec
                    )
                } else {
                    TimerUtils.getTimerTextBasedOnInput(
                        input = input.toInt(),
                        hour = it.timerHr,
                        minute = it.timerMin,
                        second = it.timerSec
                    )
                }
                val showStartTimer = newTimerText.first > 0 || newTimerText.second > 0 || newTimerText.third > 0
                it.copy(
                    timerHr = newTimerText.first,
                    timerMin = newTimerText.second,
                    timerSec = newTimerText.third,
                    showStartTimer = showStartTimer
                )
            }
        }
    }

    override fun onTimerStartClicked() {
        viewModelScope.launch {
            _timerScreenState.update {
                val newTimerText = TimerUtils.getFormattedTimerTime(
                    hour = it.timerHr,
                    minute = it.timerMin,
                    second = it.timerSec
                )
                it.copy(
                    timerHr = newTimerText.first,
                    timerMin = newTimerText.second,
                    timerSec = newTimerText.third
                )
            }
        }
    }


}