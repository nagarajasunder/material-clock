package com.geekydroid.materialclock.application.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.geekydroid.materialclock.application.utils.AlarmReScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

  @Inject
  lateinit var alarmReScheduler:AlarmReScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED ||
            intent?.action == Intent.ACTION_REBOOT ||
            intent?.action == "android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED" ||
            intent?.action == "android.intent.action.MY_PACKAGE_REPLACED") {
            Toast.makeText(context!!,"Boot Received",Toast.LENGTH_SHORT).show()
            rescheduleAlarms()
        }
    }

    private fun rescheduleAlarms() {
        alarmReScheduler.rescheduleAlarms()
    }
}