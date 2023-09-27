package com.geekydroid.materialclock.application.utils

import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AlarmFullScreenActivity will subscribe to this flow, when the user opens the alarm full screen
 * and if the user snoozes/dismisses the alarm from the notification then that action will be notified
 * to AlarmFullScreenActivity and it will be closed automatically
 */

@Singleton
class AlarmActionFlow @Inject constructor(
    @ApplicationScope
    private val externalScope: CoroutineScope,
    @IoDispatcher
    private val externalDispatcher: CoroutineDispatcher
) {

    private val _alarmActionFlow: MutableSharedFlow<AlarmActionFlowEvent> = MutableSharedFlow()
    val alarmActionFlow: SharedFlow<AlarmActionFlowEvent> = _alarmActionFlow

    fun alarmSnoozed() {
        externalScope.launch(externalDispatcher) {
            _alarmActionFlow.emit(AlarmActionFlowEvent.ALARM_SNOOZED)
        }
    }

    fun alarmDismissed() {
        externalScope.launch(externalDispatcher) {
            _alarmActionFlow.emit(AlarmActionFlowEvent.ALARM_DISMISSED)
        }
    }

}


enum class AlarmActionFlowEvent {
    ALARM_SNOOZED,
    ALARM_DISMISSED
}