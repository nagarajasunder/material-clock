package com.geekydroid.materialclock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.utils.AlarmReScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

//Todo("Issue: When we snooze the alarm the actual alarm millis which the user scheduled is changed to the snooze time")

@HiltAndroidApp
class MaterialClockApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarmNotificationChannel = NotificationChannel(
                Constants.ALARM_NOTIFICATION_CHANNEL_ID,
                "All Clock Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val alarmReminderNotificationChannel = NotificationChannel(
                Constants.ALARM_NOTIFICATION_REMINDER_CHANNEL_ID,
                "All Clock Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val timerNotificationChannel = NotificationChannel(
                Constants.TIMER_NOTIFICATION_CHANNEL_ID,
                "Timer Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(alarmNotificationChannel,alarmReminderNotificationChannel,timerNotificationChannel))
        }

    }
}