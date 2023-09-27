package com.geekydroid.materialclock.application.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_ALARM
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RawRes
import androidx.core.app.NotificationCompat
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.constants.Constants.alarmSoundsList
import com.geekydroid.materialclock.application.receivers.AlarmReceiver
import com.geekydroid.materialclock.application.utils.TIME_FORMATS
import com.geekydroid.materialclock.application.utils.TimeUtils
import com.geekydroid.materialclock.ui.alarm.AlarmFullScreenActivity
import com.geekydroid.materialclock.ui.alarm.composables.AlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.model.AlarmActionType
import com.geekydroid.materialclock.ui.alarm.model.AlarmType

private const val TAG = "AlarmNotificationHelper"

object AlarmNotificationHelper {


    private var mediaPlayer: MediaPlayer? = null

    fun postAlarmNotification(
        context: Context,
        alarmId: Int,
        alarmLabel: String,
        alarmDateMillis: Long,
        alarmTimeMillis: Long,
        alarmTriggerMillis: Long,
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays: String,
        isAlarmVibrate: Boolean
    ) {

        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val stopActionIntent = Intent(context, AlarmReceiver::class.java)
        stopActionIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        stopActionIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        stopActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        stopActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        stopActionIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        stopActionIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        stopActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        stopActionIntent.putExtra(Constants.KEY_ALARM_ACTION_TYPE, AlarmActionType.STOP.name)
        stopActionIntent.putExtra(Constants.KEY_ALARM_TYPE, AlarmType.NA)
        stopActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        stopActionIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)

        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            (alarmId * Constants.ALARM_ACTION_STOP_PENDING_INTENT_ID),
            stopActionIntent,
            getPendingIntentFlag()
        )


        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        snoozeIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        snoozeIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        snoozeIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        snoozeIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        snoozeIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        snoozeIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        snoozeIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        snoozeIntent.putExtra(Constants.KEY_ALARM_ACTION_TYPE, AlarmActionType.SNOOZE.name)
        snoozeIntent.putExtra(Constants.KEY_ALARM_TYPE, AlarmType.NA)
        snoozeIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        snoozeIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)

        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            (alarmId * Constants.ALARM_ACTION_SNOOZE_PENDING_INTENT_ID),
            snoozeIntent,
            getPendingIntentFlag()
        )

        val contentIntent = Intent(context, AlarmFullScreenActivity::class.java)
        contentIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        contentIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        contentIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        contentIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        contentIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        contentIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        contentIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        contentIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        contentIntent.putExtra(Constants.KEY_ALARM_ACTION_TYPE, AlarmActionType.SNOOZE.name)
        contentIntent.putExtra(Constants.KEY_ALARM_TYPE, AlarmType.NA)
        contentIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        contentIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)
        val contentPendingIntent = PendingIntent.getActivity(
            context, alarmId, contentIntent,
            getPendingIntentFlag()
        )

        val notificationTitle = alarmLabel.ifEmpty { "Alarm" }
        val notificationDescription = TimeUtils.getFormattedTime(
            TIME_FORMATS.EEE_HH_MM,
            alarmTriggerMillis
        )

        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_layout)
        notificationLayout.setTextViewText(R.id.tv_title, notificationTitle)
        notificationLayout.setTextViewText(R.id.tv_description, notificationDescription)

        notificationLayout.setOnClickPendingIntent(R.id.tv_snooze, snoozePendingIntent)
        notificationLayout.setOnClickPendingIntent(R.id.tv_stop, stopPendingIntent)

        val notification =
            NotificationCompat.Builder(context, Constants.ALARM_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setSmallIcon(R.drawable.baseline_access_alarm_24)
                .setWhen(0)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(contentPendingIntent, true)
                .setContentIntent(contentPendingIntent)
                .setLocalOnly(true)
                .addAction(
                    R.drawable.baseline_snooze_24,
                    context.getString(R.string.snooze),
                    snoozePendingIntent
                )
                .addAction(
                    R.drawable.ic_action_dismiss,
                    context.getString(R.string.stop),
                    stopPendingIntent
                )
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSound(null)
                .setVibrate(longArrayOf(0))
                .build()

        notificationManager.notify(alarmId, notification)
    }

    fun postAlarmReminderNotification(
        context: Context,
        alarmId: Int,
        alarmDateMillis: Long,
        alarmTimeMillis: Long,
        alarmTriggerMillis: Long,
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays: String,
        alarmType: AlarmType,
        isAlarmVibrate: Boolean,
        alarmLabel: String
    ) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val contentTitle = context.getString(R.string.upcoming_alarm)
        val contentMessage =
            "${TimeUtils.getFormattedTime(TIME_FORMATS.EEE_HH_MM, alarmTriggerMillis)} $alarmLabel"

        val dismissActionIntent = Intent(context, AlarmReceiver::class.java)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        dismissActionIntent.putExtra(
            Constants.KEY_ALARM_ACTION_TYPE,
            AlarmActionType.REMINDER_DISMISS.name
        )
        dismissActionIntent.putExtra(Constants.KEY_ALARM_TYPE, alarmType.name)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        dismissActionIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)

        val dismissActionPendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.ALARM_DISMISS_PENDING_INTENT_ID * alarmId,
            dismissActionIntent,
            getPendingIntentFlag()
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, Constants.ALARM_NOTIFICATION_REMINDER_CHANNEL_ID)
                .setContentTitle(contentTitle)
                .setContentText(contentMessage)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .addAction(
                    R.drawable.baseline_remove_circle_outline_24,
                    context.getString(R.string.dismiss),
                    dismissActionPendingIntent
                )
                .setSilent(true)

        if (isAlarmVibrate) {
            notificationBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        }

        notificationManager.notify(alarmId, notificationBuilder.build())
    }

    fun postAlarmSnoozeReminderNotification(
        context: Context,
        alarmId: Int,
        alarmDateMillis: Long,
        alarmTimeMillis: Long,
        alarmTriggerMillis: Long,
        alarmScheduleType: AlarmScheduleType,
        alarmScheduleDays: String,
        alarmType: AlarmType,
        isAlarmVibrate: Boolean,
        alarmLabel: String
    ) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val contentTitle = context.getString(R.string.alarm_snoozed)
        val contentMessage =
            "${TimeUtils.getFormattedTime(TIME_FORMATS.EEE_HH_MM, alarmTriggerMillis)} $alarmLabel"

        val dismissActionIntent = Intent(context, AlarmReceiver::class.java)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_ID, alarmId)
        dismissActionIntent.putExtra(
            Constants.KEY_SNOOZED_ALARM_ID,
            (alarmId * Constants.SNOOZE_ALARM_ID)
        )
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmDateMillis)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmTimeMillis)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmTriggerMillis)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_LABEL, alarmLabel)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmScheduleType.name)
        dismissActionIntent.putExtra(
            Constants.KEY_ALARM_ACTION_TYPE,
            AlarmActionType.SNOOZE_DISMISS.name
        )
        dismissActionIntent.putExtra(Constants.KEY_ALARM_TYPE, alarmType.name)
        dismissActionIntent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmScheduleDays)
        dismissActionIntent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, isAlarmVibrate)

        val dismissActionPendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.ALARM_DISMISS_PENDING_INTENT_ID * alarmId,
            dismissActionIntent,
            getPendingIntentFlag()
        )

        val notification =
            NotificationCompat.Builder(context, Constants.ALARM_NOTIFICATION_REMINDER_CHANNEL_ID)
                .setContentTitle(contentTitle)
                .setContentText(contentMessage)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .addAction(
                    R.drawable.baseline_remove_circle_outline_24,
                    context.getString(R.string.dismiss),
                    dismissActionPendingIntent
                )
                .setSilent(true)
                .build()

        notificationManager.notify(alarmId, notification)
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

    }

    fun playSound(context: Context, id: Int) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(USAGE_ALARM)
                .setContentType(CONTENT_TYPE_SONIFICATION)
                .build()
        )
        @RawRes val soundFile = alarmSoundsList[id].soundFile
        context.resources.openRawResourceFd(soundFile)?.let {
            mediaPlayer?.setDataSource(it.fileDescriptor,it.startOffset,it.length)
        }
        mediaPlayer?.isLooping = true
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }

    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getPendingIntentFlag(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}