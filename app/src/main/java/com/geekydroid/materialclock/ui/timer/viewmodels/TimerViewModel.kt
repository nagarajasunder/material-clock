package com.geekydroid.materialclock.ui.timer.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.application.utils.TimerUtils
import com.geekydroid.materialclock.ui.timer.models.TimerEvent
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState
import com.geekydroid.materialclock.ui.timer.models.TimerState
import com.geekydroid.materialclock.ui.timer.screenactions.TimerScreenActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TimerViewModel"

@HiltViewModel
class TimerViewModel @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val externalDispatcher: CoroutineDispatcher
) : ViewModel(), TimerScreenActions {

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
                val showStartTimer =
                    newTimerText.first > 0 || newTimerText.second > 0 || newTimerText.third > 0
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
                val timerSeconds = TimerUtils.convertTimerTextToSeconds(
                    hour = it.timerHr,
                    minute = it.timerMin,
                    seconds = it.timerSec
                )
                val timerLabel = "${TimerUtils.getTimerTextBasedOnSeconds(timerSeconds)} Timer"
                val timerText = TimerUtils.getTimerTextBasedOnSeconds(timerSeconds)
                val updatedState = it.copy(
                    timerStarted = true,
                    timerEvent = TimerEvent(
                        timerSec = timerSeconds,
                        currentTimerSec = timerSeconds,
                        timerProgress = 1f,
                        timerLabel = timerLabel,
                        timerState = TimerState.STARTED,
                        timerText = timerText
                    )
                )
                startTimer()
                updatedState
            }
        }
    }

    private suspend fun startTimer() {
        externalScope.launch(externalDispatcher) {
            while (true) {
                delay(1000)
                if (_timerScreenState.value.timerEvent.timerState == TimerState.IDLE) {
                    break
                }
                _timerScreenState.update {
                    val newTimerSeconds = it.timerEvent.currentTimerSec - 1
                    val timerText = TimerUtils.getTimerTextBasedOnSeconds(newTimerSeconds)
                    var timerProgress = 0f
                    if (newTimerSeconds >= 0) {
                        timerProgress =
                            TimerUtils.getTimerProgress(it.timerEvent.timerSec,newTimerSeconds)
                    }
                    it.copy(
                        timerEvent = it.timerEvent.copy(
                            currentTimerSec = newTimerSeconds,
                            timerText = timerText,
                            timerProgress = timerProgress
                        )
                    )
                }
            }
        }
    }


}