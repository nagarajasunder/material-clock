package com.geekydroid.materialclock.application.utils

import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.ui.timer.models.TimerEvent
import com.geekydroid.materialclock.ui.timer.models.TimerState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerLogicHandler @Inject constructor(
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val externalDispatcher: CoroutineDispatcher
) {

    private val _timerEvent: MutableStateFlow<TimerEvent> =
        MutableStateFlow(TimerEvent.initialState)
    val timerEvent: StateFlow<TimerEvent> = _timerEvent


    fun startTimer(updatedTimerEvent: TimerEvent) {
        externalScope.launch(externalDispatcher) {
            _timerEvent.update { updatedTimerEvent }
            while (_timerEvent.value.timerState != TimerState.IDLE) {
                delay(1000)
                _timerEvent.update {
                    if (it.timerState == TimerState.STARTED || it.timerState == TimerState.EXCEEDED) {
                        val newTimerMillis = it.currentTimerMillis - 1000
                        val timerProgress: Float =
                            TimerUtils.getTimerProgress(it.timerMillis, newTimerMillis)
                        val timerText = TimerUtils.getTimerTextBasedOnMillis(newTimerMillis)
                        val newTimerState =
                            if (newTimerMillis < 0) TimerState.EXCEEDED else it.timerState
                        it.copy(
                            currentTimerMillis = newTimerMillis,
                            timerText = timerText,
                            timerProgress = timerProgress,
                            timerState = newTimerState
                        )
                    } else {
                        it
                    }
                }
            }
        }
    }


    fun addOneMinuteToTimer() {
        externalScope.launch {
            _timerEvent.update {
                val newTimerMillis = if (it.currentTimerMillis < 0) {
                    60_000
                } else {
                    it.currentTimerMillis + 60_000
                }
                val newActualTimerMillis = if (it.currentTimerMillis < 0) {
                    60_000
                } else {
                    it.timerMillis + 60_000
                }
                val timerText = TimerUtils.getTimerTextBasedOnMillis(newTimerMillis)
                val timerProgress: Float =
                    TimerUtils.getTimerProgress(newActualTimerMillis, newTimerMillis)
                it.copy(
                    currentTimerMillis = newTimerMillis,
                    timerText = timerText,
                    timerMillis = newActualTimerMillis,
                    timerProgress = timerProgress,
                    timerState = TimerState.STARTED
                )
            }
        }
    }

    fun pauseTimer() {
        externalScope.launch(externalDispatcher) {
            _timerEvent.update {
                it.copy(
                    timerState = TimerState.PAUSED
                )
            }
        }
    }

    fun resumeTimer() {
        externalScope.launch(externalDispatcher) {
            _timerEvent.update {
                it.copy(
                    timerState = TimerState.STARTED
                )
            }
        }
    }

    fun closeTimer() {
        externalScope.launch(externalDispatcher) {
            _timerEvent.update {
                it.copy(
                    timerState = TimerState.IDLE
                )
            }
        }
    }

    fun resetTimer() {
        externalScope.launch(externalDispatcher) {
            _timerEvent.update {
                val timerText = TimerUtils.getTimerTextBasedOnMillis(it.initialTimerMillis)
                val timerLabel = "${TimerUtils.getTimerTextLabelBasedOnMillis(it.initialTimerMillis)}Timer"
                it.copy(
                    timerState = TimerState.RESET,
                    timerMillis = it.initialTimerMillis,
                    currentTimerMillis = it.initialTimerMillis,
                    timerProgress = 1f,
                    timerLabel = timerLabel,
                    timerText = timerText
                )
            }
        }
    }
}