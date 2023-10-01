package com.geekydroid.materialclock.ui.timer.models

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

/**
 * @param initialTimerMillis -> The timer which the user set it doesn't changes, It is needed when the user clicks reset
 * @param timerMillis -> This will be the updated timer millis when the user clicks add 1 min action
 * @param currentTimerMillis -> This will the millis used to show the timer text to user
 *
 */

@Parcelize
data class TimerEvent(
    val initialTimerMillis:Long,
    val timerMillis: Long,
    val currentTimerMillis: Long,
    val timerProgress:Float,
    val timerState: TimerState,
    val timerText: String,
    val timerLabel:String
) : Parcelable {
    companion object {
        val initialState = TimerEvent(
            initialTimerMillis = 0,
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
    RESET,
    EXCEEDED
}