package com.geekydroid.materialclock.ui.timer.screenactions

interface TimerScreenActions {

    fun onTimerInputChanged(input:String)

    fun onTimerStartClicked()

    fun onAddOneMinuteClicked()

    fun onPauseTimerClicked()

    fun onResumeClicked()

    fun onResetTimerClicked()

    fun onCloseTimerClicked()
}