package com.geekydroid.materialclock.ui.timer.models

data class TimerEvent(
    val timerSec: Int,
    val currentTimerSec: Int,
    val timerProgress:Float,
    val timerState: TimerState,
    val timerText: String,
    val timerLabel:String
) {
    companion object {
        val initialState = TimerEvent(
            timerSec = 0,
            currentTimerSec = 0,
            timerProgress = 1f,
            timerState = TimerState.IDLE,
            timerText = "",
            timerLabel = ""
        )
    }
}

enum class TimerState {
    /*
    * IDLE -> Default State no action performed yet
    * STARTED -> Timer is on going
    * PAUSED -> Timer paused
    * EXCEEDED -> Timer Completed and on going(going negative)
    * */

    IDLE,
    STARTED,
    PAUSED,
    EXCEEDED
}