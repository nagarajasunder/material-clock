package com.geekydroid.materialclock.application.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.application.notification.AlarmNotificationHelper
import com.geekydroid.materialclock.application.utils.AlarmActionFlow
import com.geekydroid.materialclock.application.utils.AlarmScheduler
import com.geekydroid.materialclock.application.utils.AlarmUtils
import com.geekydroid.materialclock.application.utils.AlarmUtils.getAlarmActionType
import com.geekydroid.materialclock.application.utils.AlarmUtils.getAlarmScheduleType
import com.geekydroid.materialclock.application.utils.AlarmUtils.getAlarmType
import com.geekydroid.materialclock.application.utils.MediaPlayerUtils
import com.geekydroid.materialclock.application.utils.MediaType
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.model.AlarmActionType
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import com.geekydroid.materialclock.ui.alarm.model.AlarmType
import com.geekydroid.materialclock.ui.alarm.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    @ApplicationScope
    lateinit var externalScope: CoroutineScope

    @Inject
    @IoDispatcher
    lateinit var externalDispatcher: CoroutineDispatcher

    @Inject
    lateinit var alarmRepository: AlarmRepository

    @Inject
    lateinit var alarmActionFlow: AlarmActionFlow

    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            Toast.makeText(context,"The Action is ${intent.action}",Toast.LENGTH_LONG).show()
            val alarmActionTypeStr = intent.getStringExtra(Constants.KEY_ALARM_ACTION_TYPE)?:""
            val alarmActionType = getAlarmActionType(alarmActionTypeStr)
            val alarmId = intent.getIntExtra(Constants.KEY_ALARM_ID, -1)
            val snoozedAlarmId = intent.getIntExtra(Constants.KEY_SNOOZED_ALARM_ID,-1)
            val alarmLabel = intent.getStringExtra(Constants.KEY_ALARM_LABEL) ?: ""
            val isAlarmVibrate = intent.getBooleanExtra(Constants.KEY_IS_ALARM_VIBRATE, false)
            val alarmScheduleDays = intent.getStringExtra(Constants.KEY_ALARM_SCHEDULE_DAYS) ?: ""
            val alarmScheduleTypeStr = intent.getStringExtra(Constants.KEY_ALARM_SCHEDULE_TYPE) ?: ""
            val alarmScheduleType = getAlarmScheduleType(alarmScheduleTypeStr)
            val alarmTypeStr = intent.getStringExtra(Constants.KEY_ALARM_TYPE)?:""
            val alarmType = getAlarmType(alarmTypeStr)
            val alarmDateMillis = intent.getLongExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, -1L)
            val alarmTimeMillis = intent.getLongExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, -1L)
            val alarmTriggerMillis = intent.getLongExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, -1L)
            var alarmSoundId = 0
            runBlocking {
                alarmSoundId = alarmRepository.getAlarmSoundId(alarmId).first()
            }


            when(alarmType) {
                AlarmType.REMINDER -> {
                    //Once the Reminder notification trigger we can schedule the actual alarm
                    AlarmNotificationHelper.postAlarmReminderNotification(
                        context = context!!,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = alarmTriggerMillis,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmScheduleType = alarmScheduleType,
                        isAlarmVibrate = isAlarmVibrate,
                        alarmType = AlarmType.NA
                    )
                    AlarmScheduler.scheduleAlarm(
                        context = context,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        isAlarmVibrate = isAlarmVibrate,
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = alarmTriggerMillis,
                        alarmType = AlarmType.ACTUAL
                    )
                }
                AlarmType.ACTUAL -> {
                    AlarmNotificationHelper.postAlarmNotification(
                        context = context!!,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = alarmTriggerMillis,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmScheduleType = alarmScheduleType,
                        isAlarmVibrate = isAlarmVibrate
                    )
                    MediaPlayerUtils.playSound(context,alarmSoundId,MediaType.ALARM)
                    vibrate(context)
                    scheduleNextAlarm(
                        context = context,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        alarmVibrate = isAlarmVibrate,
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = alarmTriggerMillis
                    )
                }
                AlarmType.SNOOZE -> {
                    AlarmNotificationHelper.postAlarmNotification(
                        context = context!!,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = alarmTriggerMillis,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmScheduleType = alarmScheduleType,
                        isAlarmVibrate = isAlarmVibrate
                    )
                    vibrate(context)
                    MediaPlayerUtils.playSound(context,alarmSoundId,MediaType.ALARM)
                    updateSnoozeDetails(
                        alarmId = alarmId,
                        alarmScheduleType = alarmScheduleType,
                        isSnoozed = false,
                        snoozeTriggerMillis = 0L
                    )
                }

                AlarmType.NA -> {}
            }

            when(alarmActionType) {
                AlarmActionType.REMINDER_DISMISS -> {
                    AlarmScheduler.cancelAlarm(context!!,alarmId)
                    AlarmNotificationHelper.cancelNotification(context,alarmId)
                    stopVibrate(context)
                    /**
                     * If we just post a reminder alarm and the user dismisses the alarm and if the alarm is repeating
                     * then we should schedule it next time
                     */
                    scheduleNextAlarm(
                        context = context,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        alarmVibrate = isAlarmVibrate,
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = alarmTriggerMillis
                    )
                }
                AlarmActionType.SNOOZE -> {
                    MediaPlayerUtils.stopAlarmSound(context!!)
                    AlarmNotificationHelper.cancelNotification(context,alarmId)
                    val snoozeTriggerMillis =  AlarmUtils.getAlarmSnoozeTime()
                    AlarmScheduler.scheduleAlarm(
                        context = context,
                        alarmId = alarmId,
                        alarmLabel = alarmLabel,
                        isAlarmVibrate = isAlarmVibrate,
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = snoozeTriggerMillis,
                        alarmType = AlarmType.SNOOZE
                    )
                    AlarmNotificationHelper.postAlarmSnoozeReminderNotification(
                        context = context,
                        alarmId = alarmId,
                        alarmTriggerMillis = snoozeTriggerMillis,
                        alarmLabel = alarmLabel,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        isAlarmVibrate = isAlarmVibrate,
                        alarmType = AlarmType.NA
                    )
                    updateSnoozeDetails(
                        alarmId = alarmId,
                        alarmScheduleType = alarmScheduleType,
                        isSnoozed = true,
                        snoozeTriggerMillis = snoozeTriggerMillis
                    )
                    alarmActionFlow.alarmSnoozed()
                }
                AlarmActionType.STOP -> {
                    MediaPlayerUtils.stopAlarmSound(context!!)
                    stopVibrate(context)
                    AlarmNotificationHelper.cancelNotification(context,alarmId)
                    alarmActionFlow.alarmDismissed()
                }

                AlarmActionType.SNOOZE_DISMISS -> {
                    AlarmScheduler.cancelAlarm(context!!,snoozedAlarmId)
                    AlarmNotificationHelper.cancelNotification(context,alarmId)
                    updateSnoozeDetails(alarmId,alarmScheduleType,false,0L)
                }
                AlarmActionType.NA -> {}
            }
        }
    }



    private fun scheduleNextAlarm(
        context: Context,
        alarmId: Int,
        alarmLabel: String,
        alarmVibrate: Boolean,
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays: String,
        alarmDateMillis: Long,
        alarmTimeMillis: Long,
        alarmTriggerMillis:Long
    ) {
        externalScope.launch(externalDispatcher) {
            when (alarmScheduleType) {
                AlarmScheduleType.ONCE -> {
                    val alarmMaster = AlarmMaster(
                        alarmId = alarmId,
                        alarmStatus = AlarmStatus.OFF,
                        alarmLabel = alarmLabel,
                        alarmTimeInMillis = alarmTimeMillis,
                        alarmDateInMillis = alarmDateMillis,
                        alarmTriggerMillis = alarmTriggerMillis,
                        alarmScheduledDays = alarmScheduleDays,
                        alarmType = alarmScheduleType,
                        isAlarmVibrate = alarmVibrate,
                        alarmSoundIndex = 0,
                        createdOn = System.currentTimeMillis(),
                        updatedOn = System.currentTimeMillis()
                    )
                    updateAlarm(alarmMaster)
                }

                AlarmScheduleType.SCHEDULE_ONCE -> {
                    val alarmMaster = AlarmMaster(
                        alarmId = alarmId,
                        alarmStatus = AlarmStatus.OFF,
                        alarmLabel = alarmLabel,
                        alarmTimeInMillis = alarmTimeMillis,
                        alarmDateInMillis = alarmDateMillis,
                        alarmTriggerMillis = alarmTriggerMillis,
                        alarmScheduledDays = alarmScheduleDays,
                        alarmType = alarmScheduleType,
                        isAlarmVibrate = alarmVibrate,
                        alarmSoundIndex = 0,
                        createdOn = System.currentTimeMillis(),
                        updatedOn = System.currentTimeMillis()
                    )
                    updateAlarm(alarmMaster)
                }
                AlarmScheduleType.REPEATED -> {
                    val nextAlarmTriggerMillis = AlarmUtils.getAlarmTimeBasedOnConstraints(
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        alarmTimeMillis = alarmTriggerMillis,
                        alarmDateMillis = alarmDateMillis
                    )
                    AlarmScheduler.scheduleAlarmWithReminder(
                        context = context,
                        alarmId = alarmId,
                        alarmDateMillis = alarmDateMillis,
                        alarmTimeMillis = alarmTimeMillis,
                        alarmTriggerMillis = nextAlarmTriggerMillis,
                        alarmLabel = alarmLabel,
                        alarmScheduleType = alarmScheduleType,
                        alarmScheduleDays = alarmScheduleDays,
                        isAlarmVibrate = alarmVibrate
                    )
                    val alarmMaster = AlarmMaster(
                        alarmId = alarmId,
                        alarmStatus = AlarmStatus.ON,
                        alarmLabel = alarmLabel,
                        alarmTimeInMillis = alarmTimeMillis,
                        alarmDateInMillis = alarmDateMillis,
                        alarmTriggerMillis = nextAlarmTriggerMillis,
                        alarmScheduledDays = alarmScheduleDays,
                        alarmType = alarmScheduleType,
                        isAlarmVibrate = alarmVibrate,
                        alarmSoundIndex = 0,
                        createdOn = System.currentTimeMillis(),
                        updatedOn = System.currentTimeMillis()
                    )
                    updateAlarm(alarmMaster)
                }
            }
        }


    }

    private suspend fun updateAlarm(alarmMaster: AlarmMaster) {
        alarmRepository.updateExistingAlarm(alarmMaster)
    }

    private fun updateSnoozeDetails(alarmId: Int, alarmScheduleType: AlarmScheduleType,isSnoozed: Boolean, snoozeTriggerMillis: Long) {
        externalScope.launch(externalDispatcher) {
            val alarmStatus = if (isSnoozed) {
                AlarmStatus.ON
            }
            else {
                when(alarmScheduleType) {
                    AlarmScheduleType.ONCE,AlarmScheduleType.SCHEDULE_ONCE -> AlarmStatus.OFF
                    AlarmScheduleType.REPEATED -> AlarmStatus.ON
                }
            }
            alarmRepository.updateSnoozeDetails(alarmId,alarmStatus,isSnoozed,snoozeTriggerMillis)
        }
    }

    private fun vibrate(context:Context) {

        val vibratorPattern = longArrayOf(1000,1000)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(vibratorPattern,0)
            )
        }
        else {
            vibrator.vibrate(1000)
        }
    }

    private fun stopVibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
    }

}