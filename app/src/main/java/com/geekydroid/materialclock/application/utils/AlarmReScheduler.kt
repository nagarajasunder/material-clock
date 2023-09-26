package com.geekydroid.materialclock.application.utils

import android.content.Context
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmReScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmRepository: AlarmRepository,
    @ApplicationScope private val externalScope:CoroutineScope,
    @IoDispatcher private val externalDispatcher:CoroutineDispatcher
) {

    fun rescheduleAlarms() {
        externalScope.launch(externalDispatcher) {
            val activeAlarms = alarmRepository.getAllActiveAlarms()
            activeAlarms.forEach { alarm ->
                AlarmScheduler.scheduleAlarmWithReminder(
                    context = context,
                    alarmId = alarm.alarmId,
                    alarmDateMillis = alarm.alarmDateInMillis,
                    alarmTimeMillis = alarm.alarmTimeInMillis,
                    alarmTriggerMillis = alarm.alarmTriggerMillis,
                    alarmLabel = alarm.alarmLabel,
                    alarmScheduleDays = alarm.alarmScheduledDays,
                    alarmScheduleType = alarm.alarmType,
                    isAlarmVibrate = alarm.isAlarmVibrate
                )
            }
        }
    }

}