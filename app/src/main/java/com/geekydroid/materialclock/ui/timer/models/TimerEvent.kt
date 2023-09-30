package com.geekydroid.materialclock.ui.timer.models

data class TimerEvent(
    val timerMillis: Long,
    val currentTimerMillis: Long,
    val timerProgress:Float,
    val timerState: TimerState,
    val timerText: String,
    val timerLabel:String
) {
    companion object {
        val initialState = TimerEvent(
            timerMillis = 0,
            currentTimerMillis = 0,
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