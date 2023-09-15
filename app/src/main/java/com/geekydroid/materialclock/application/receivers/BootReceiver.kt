package com.geekydroid.materialclock.application.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.application.utils.AlarmScheduler
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    @ApplicationScope
    lateinit var externalScope: CoroutineScope

    @Inject
    @IoDispatcher
    lateinit var externalDispatcher: CoroutineDispatcher

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == "android.intent.action.QUICKBOOT_POWERON") {
            rescheduleAlarms(context!!)

        }
    }

    private fun rescheduleAlarms(context: Context) {
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