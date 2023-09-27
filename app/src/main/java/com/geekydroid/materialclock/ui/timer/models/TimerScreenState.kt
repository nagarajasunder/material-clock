package com.geekydroid.materialclock.ui.timer.models

import com.geekydroid.materialclock.application.constants.Constants

data class TimerScreenState(
    val timerHr:Int,
    val timerMin:Int,
    val timerSec:Int
) {
    companion object {
        val initialState  = TimerScreenState(Constants.TIMER_DEFAULT_HOUR,Constants.TIMER_DEFAULT_MINUTE,Constants.TIMER_DEFAULT_SECOND)
    }
}