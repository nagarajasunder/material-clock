package com.geekydroid.materialclock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.geekydroid.materialclock.application.constants.Constants
import dagger.hilt.android.HiltAndroidApp

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
            val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(alarmNotificationChannel,alarmReminderNotificationChannel))
        }

    }
}