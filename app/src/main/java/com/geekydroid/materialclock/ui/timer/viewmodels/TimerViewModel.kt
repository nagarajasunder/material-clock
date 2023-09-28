package com.geekydroid.materialclock.ui.timer.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.geekydroid.materialclock.application.utils.TimerUtils
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState
import com.geekydroid.materialclock.ui.timer.screenactions.TimerScreenActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel(), TimerScreenActions {

    private val _timerScreenState = MutableStateFlow(TimerScreenState.initialState)
    val timerScreenState: StateFlow<TimerScreenState> = _timerScreenState

    override fun onTimerInputChanged(input: String) {
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
            it.copy(
                timerHr = newTimerText.first,
                timerMin = newTimerText.second,
                timerSec = newTimerText.third
            )
        }
    }


}