package com.geekydroid.materialclock.ui.timer.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekydroid.materialclock.application.utils.TimerLogicHandler
import com.geekydroid.materialclock.application.utils.TimerUtils
import com.geekydroid.materialclock.ui.timer.models.TimerEvent
import com.geekydroid.materialclock.ui.timer.models.TimerScreenState
import com.geekydroid.materialclock.ui.timer.models.TimerState
import com.geekydroid.materialclock.ui.timer.screenactions.TimerScreenActions
import com.geekydroid.materialclock.ui.timer.screenevents.TimerScreenEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TimerViewModel"

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerLogicHandler: TimerLogicHandler
) : ViewModel(), TimerScreenActions {

    private val _timerScreenState = MutableStateFlow(TimerScreenState.initialState)
    val timerScreenState: StateFlow<TimerScreenState> = _timerScreenState
    val timerEvent : StateFlow<TimerEvent> = timerLogicHandler.timerEvent

    private val eventsChannel: Channel<TimerScreenEvents> = Channel()
    val events: Flow<TimerScreenEvents> = eventsChannel.receiveAsFlow()

    override fun onTimerInputChanged(input: String) {
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
                val timerMillis = TimerUtils.convertTimerTextToMillis(
                    hour = it.timerHr,
                    minute = it.timerMin,
                    seconds = it.timerSec
                )
                Log.d(TAG, "onTimerStartClicked: timer millis $timerMillis")
                val timerLabel = "${TimerUtils.getTimerTextLabelBasedOnMillis(timerMillis)}Timer"
                val timerText = TimerUtils.getTimerTextBasedOnMillis(timerMillis)
                val updatedState = it.copy(
                    timerStarted = true
                )
                val timerEvent = TimerEvent(
                    initialTimerMillis = timerMillis,
                    timerMillis = timerMillis,
                    currentTimerMillis = timerMillis,
                    timerProgress = 1f,
                    timerLabel = timerLabel,
                    timerState = TimerState.STARTED,
                    timerText = timerText
                )
                eventsChannel.send(TimerScreenEvents.StartTimerService(timerEvent))
                updatedState
            }
        }
    }

    override fun onAddOneMinuteClicked() {
        timerLogicHandler.addOneMinuteToTimer()
    }

    override fun onResumeClicked() {
        timerLogicHandler.resumeTimer()
    }

    override fun onPauseTimerClicked() {
        timerLogicHandler.pauseTimer()
    }

    override fun onResetTimerClicked() {
        viewModelScope.launch {
            timerLogicHandler.resetTimer()
            if(timerEvent.value.timerState == TimerState.EXCEEDED) {
                eventsChannel.send(TimerScreenEvents.StopTimerSound)
            }
        }
    }

    override fun onCloseTimerClicked() {
       viewModelScope.launch {
           timerLogicHandler.closeTimer()
           resetTimerScreenState()
       }
    }

    private fun resetTimerScreenState() {
        _timerScreenState.update {
            TimerScreenState.initialState
        }
    }

}