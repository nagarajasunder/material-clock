package com.geekydroid.materialclock.ui.timer.screenevents

import com.geekydroid.materialclock.ui.timer.models.TimerEvent

sealed interface TimerScreenEvents {

    data class startTimerService(val timerEvent: TimerEvent) : TimerScreenEvents
}