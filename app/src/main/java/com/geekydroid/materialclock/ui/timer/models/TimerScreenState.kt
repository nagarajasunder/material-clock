package com.geekydroid.materialclock.ui.timer.models

import com.geekydroid.materialclock.application.constants.Constants

data class TimerScreenState(
    val timerHr:Int,
    val timerMin:Int,
    val timerSec:Int,
    val showStartTimer:Boolean,
    val timerStarted:Boolean,
    val timerEvent: TimerEvent
) {
    companion object {
        val initialState  = TimerScreenState(
            timerHr = Constants.TIMER_DEFAULT_HOUR,
            timerMin = Constants.TIMER_DEFAULT_MINUTE,
            timerSec = Constants.TIMER_DEFAULT_SECOND,
            showStartTimer = false,
            timerStarted = false,
            timerEvent = TimerEvent.initialState
        )
    }
}