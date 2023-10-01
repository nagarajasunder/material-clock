package com.geekydroid.materialclock.application.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.geekydroid.materialclock.R
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.services.TimerService
import com.geekydroid.materialclock.ui.alarm.AlarmFullScreenActivity
import com.geekydroid.materialclock.ui.timer.TimerFullScreenActivity
import com.geekydroid.materialclock.ui.timer.models.TimerEvent
import com.geekydroid.materialclock.ui.timer.models.TimerState

object TimerNotificationHelper {

    fun createTimerNotification(
        context: Context,
        timerEvent: TimerEvent,
        notificationTitle: String
    ): Notification {

        val message: String = when (timerEvent.timerState) {
            TimerState.IDLE -> {
                ""
            }

            TimerState.STARTED -> {
                context.getString(R.string.timer_label)
            }

            TimerState.PAUSED -> {
                context.getString(R.string.timer_paused)
            }

            TimerState.RESET -> {
                ""
            }

            TimerState.EXCEEDED -> {
                context.getString(R.string.time_up)
            }
        }

        val pauseActionIntent = Intent(context, TimerService::class.java)
        pauseActionIntent.putExtra(Constants.TIMER_ACTION_TYPE, Constants.TIMER_ACTION_PAUSE_TIMER)
        val pauseActionPendingIntent = PendingIntent.getService(
            context,
            Constants.TIMER_PAUSE_PENDING_INTENT_ID,
            pauseActionIntent,
            getPendingIntentFlag()
        )

        val addMinActionIntent = Intent(context, TimerService::class.java)
        addMinActionIntent.putExtra(Constants.TIMER_ACTION_TYPE, Constants.TIMER_ACTION_ADD_MIN)
        val addMinActionPendingIntent = PendingIntent.getService(
            context,
            Constants.TIMER_ADD_TIME_PENDING_INTENT_ID,
            addMinActionIntent,
            getPendingIntentFlag()
        )

        val resumeActionIntent = Intent(context, TimerService::class.java)
        resumeActionIntent.putExtra(
            Constants.TIMER_ACTION_TYPE,
            Constants.TIMER_ACTION_RESUME_TIMER
        )
        val resumeActionPendingIntent = PendingIntent.getService(
            context,
            Constants.TIMER_RESUME_PENDING_INTENT_ID,
            resumeActionIntent,
            getPendingIntentFlag()
        )

        val resetActionIntent = Intent(context, TimerService::class.java)
        resetActionIntent.putExtra(Constants.TIMER_ACTION_TYPE, Constants.TIMER_ACTION_RESET_TIMER)
        val resetActionPendingIntent = PendingIntent.getService(
            context,
            Constants.TIMER_RESET_PENDING_INTENT_ID,
            resetActionIntent,
            getPendingIntentFlag()
        )

        val notificationBuilder =
            NotificationCompat.Builder(context, Constants.TIMER_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(message)
                .setSmallIcon(R.drawable.hourglass_anim)
                .setWhen(0)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSound(null)

        if (timerEvent.timerState == TimerState.PAUSED) {
            notificationBuilder.addAction(
                R.drawable.baseline_play_arrow_24,
                context.getString(R.string.resume), resumeActionPendingIntent
            )
                .addAction(
                    R.drawable.restart_alt_24px,
                    context.getString(R.string.reset),
                    resetActionPendingIntent
                )
        } else {
            notificationBuilder.addAction(
                R.drawable.pause_24px,
                context.getString(R.string.pause), pauseActionPendingIntent
            ).addAction(
                R.drawable.baseline_add_circle_outline_24,
                context.getString(R.string.timer_add_time),
                addMinActionPendingIntent
            )
        }

        return notificationBuilder.build()
    }

    fun postTimeUpNotification(
        context: Context,
        timerEvent: TimerEvent
    ) {
        val stopActionIntent = Intent(context, TimerService::class.java)
        stopActionIntent.putExtra(Constants.TIMER_ACTION_TYPE, Constants.TIMER_ACTION_RESET_TIMER)
        val stopActionPendingIntent = PendingIntent.getService(
            context,
            Constants.TIMER_RESET_PENDING_INTENT_ID,
            stopActionIntent,
            getPendingIntentFlag()
        )

        val addMinActionIntent = Intent(context, TimerService::class.java)
        addMinActionIntent.putExtra(Constants.TIMER_ACTION_TYPE, Constants.TIMER_ACTION_ADD_MIN)
        val addMinActionPendingIntent = PendingIntent.getService(
            context,
            Constants.TIMER_ADD_TIME_PENDING_INTENT_ID,
            addMinActionIntent,
            getPendingIntentFlag()
        )

        val contentIntent = Intent(context, TimerFullScreenActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,Constants.TIMER_TIME_UP_PENDING_INTENT_ID, contentIntent,
            getPendingIntentFlag()
        )


        val timeUpNotification =
            NotificationCompat.Builder(context, Constants.TIMER_TIME_UP_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(context.getString(R.string.time_up))
                .setContentText(timerEvent.timerText)
                .setSmallIcon(R.drawable.hourglass_anim)
                .setWhen(0)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setLocalOnly(true)
                .setOngoing(true)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setFullScreenIntent(contentPendingIntent,true)
                .setContentIntent(contentPendingIntent)
                .addAction(
                    R.drawable.restart_alt_24px,
                    context.getString(R.string.stop),
                    stopActionPendingIntent
                )
                .addAction(
                    R.drawable.baseline_add_circle_outline_24,
                    context.getString(R.string.timer_add_time),
                    addMinActionPendingIntent
                ).build()

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Constants.TIMER_NOTIFICATION_ID,timeUpNotification)

    }

    fun updateTimerNotification(
        context: Context,
        timerEvent: TimerEvent,
        title: String
    ) {
        val notification = createTimerNotification(context, timerEvent, title)
        val notificationManger =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.notify(Constants.TIMER_NOTIFICATION_ID, notification)
    }

    fun removeTimerNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(Constants.TIMER_NOTIFICATION_ID)
    }

    private fun getPendingIntentFlag(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }


}