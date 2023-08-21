package com.geekydroid.materialclock.application.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.notification.AlarmNotificationHelper
import com.geekydroid.materialclock.application.receivers.AlarmReceiver
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.model.AlarmActionType
import com.geekydroid.materialclock.ui.alarm.model.AlarmType

private const val TAG = "AlarmScheduler"

object AlarmScheduler {

    @SuppressLint("MissingPermission")
    fun scheduleAlarmWithReminder(
        context: Context,
        alarmId: Int,
        alarmDateMillis: Long,
        alarmTimeMillis: Long,
        alarmTriggerMillis: Long,
        alarmLabel: String,
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays: String,
        isAlarmVibrate:Boolean,
    ) {

        var alarmType = AlarmType.ACTUAL
        var finalAlarmTriggerMillis = alarmTriggerMillis

        if (AlarmUtils.isEligibleForReminderAlarm(finalAlarmTriggerMillis)) {
            Log.d(TAG, "scheduleAlarmWithReminder: isEligible")
            finalAlarmTriggerMillis = alarmTriggerMillis - (Constants.ALARM_REMINDER_HOUR*60_000)
            alarmType = AlarmType.REMINDER
        }
        else {
            AlarmNotificationHelper.postAlarmReminderNotification(
                context = context,
                alarmId = alarmId,
                alarmTriggerMillis = finalAlarmTriggerMillis,
                alarmLabel = alarmLabel,
                alarmDateMillis = alarmDateMillis,
                alarmTimeMillis = alarmTimeMillis,
                alarmScheduleType = alarmScheduleType,
                alarmScheduleDays = alarmScheduleDays,
                isAlarmVibrate = isAlarmVibrate,
                alarmType = AlarmType.NA
            )
        }

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        alarmIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        alarmIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        alarmIntent.putExtra(Constants.KEY_ALARM_TYPE,alarmType.name)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        alarmIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)
        alarmIntent.putExtra(Constants.KEY_ALARM_ACTION_TYPE,AlarmActionType.NA)

        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (canScheduleExactAlarms(alarmManager)) {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(finalAlarmTriggerMillis,alarmPendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo,alarmPendingIntent)
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP,finalAlarmTriggerMillis,alarmPendingIntent)
        }


    }

    fun cancelAlarm(
        context:Context,
        alarmId:Int,
    ) {

        /**
         * 1. Cancel Reminder Notification
         * 2. Cancel Actual Alarm
         * 3. Cancel Snooze Alarm
         */

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(alarmId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmSnoozePendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId*Constants.SNOOZE_ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (alarmPendingIntent != null) {
            alarmManager.cancel(alarmPendingIntent)
        }
        if (alarmSnoozePendingIntent != null) {
            alarmManager.cancel(alarmSnoozePendingIntent)
        }

    }

    fun cancelSnoozedAlarm(context: Context,alarmId: Int)  {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(alarmId)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmSnoozePendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId*Constants.SNOOZE_ALARM_ID,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        if (alarmSnoozePendingIntent != null) {
            alarmManager.cancel(alarmSnoozePendingIntent)
        }
    }

    private fun canScheduleExactAlarms(alarmManager: AlarmManager): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    @SuppressLint("MissingPermission")
    fun scheduleAlarm(
        context: Context,
        alarmId: Int,
        alarmDateMillis: Long,
        alarmTimeMillis: Long,
        alarmTriggerMillis: Long,
        alarmLabel: String,
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays: String,
        isAlarmVibrate:Boolean,
        alarmType:AlarmType,
    ) {
        /**
         * This finalAlarmId is to modify the alarm id which scheduling the alarm based on it's type
         */
        val finalAlarmId = when(alarmType) {
            AlarmType.SNOOZE -> alarmId*Constants.SNOOZE_ALARM_ID
            else -> alarmId
        }
        val snoozedAlarmId = if (alarmType == AlarmType.SNOOZE) alarmId*Constants.SNOOZE_ALARM_ID else 0

        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        alarmIntent.putExtra(Constants.KEY_SNOOZED_ALARM_ID,snoozedAlarmId)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        alarmIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        alarmIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        alarmIntent.putExtra(Constants.KEY_ALARM_TYPE,alarmType.name)
        alarmIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        alarmIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)
        alarmIntent.putExtra(Constants.KEY_ALARM_ACTION_TYPE,AlarmActionType.NA)

        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            finalAlarmId,
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (canScheduleExactAlarms(alarmManager)) {
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTriggerMillis,alarmPendingIntent)
            val alarmClockInfo = AlarmManager.AlarmClockInfo(alarmTriggerMillis,alarmPendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo,alarmPendingIntent)
        }
    }
}