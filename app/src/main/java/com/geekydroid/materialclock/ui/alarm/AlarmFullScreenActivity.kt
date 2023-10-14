package com.geekydroid.materialclock.ui.alarm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.receivers.AlarmReceiver
import com.geekydroid.materialclock.application.utils.AlarmActionFlow
import com.geekydroid.materialclock.application.utils.AlarmActionFlowEvent
import com.geekydroid.materialclock.application.utils.AlarmUtils.getAlarmScheduleType
import com.geekydroid.materialclock.ui.alarm.composables.AlarmExpandedScreen
import com.geekydroid.materialclock.ui.alarm.composables.AlarmStatus
import com.geekydroid.materialclock.ui.alarm.model.AlarmActionType
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import com.geekydroid.materialclock.ui.alarm.model.AlarmType
import com.geekydroid.materialclock.ui.alarm.viewmodel.AlarmFullScreenViewModel
import com.geekydroid.materialclock.ui.theme.McAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmFullScreenActivity : ComponentActivity() {

    @Inject
    lateinit var alarmActionFlow: AlarmActionFlow

    private val alarmFullScreenViewModel: AlarmFullScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                alarmActionFlow.alarmActionFlow.collect { event ->
                    when (event) {
                        AlarmActionFlowEvent.ALARM_SNOOZED -> {
                            Toast.makeText(this@AlarmFullScreenActivity,"Alarm Snoozed",Toast.LENGTH_SHORT).show()
                            closeActivity()
                        }
                        AlarmActionFlowEvent.ALARM_DISMISSED -> {
                            Toast.makeText(this@AlarmFullScreenActivity,"Alarm Dismissed",Toast.LENGTH_SHORT).show()
                            closeActivity()
                        }
                    }
                }
            }
        }
        turnScreenOn()
        setContent {
            McAppTheme {
                val digitalTime by alarmFullScreenViewModel.timeFlow.collectAsStateWithLifecycle(
                    initialValue = System.currentTimeMillis()
                )
                AlarmExpandScreenContent(alarmFullScreenViewModel,digitalTime)
            }
        }
        updateAlarmData(intent)
    }

    private fun turnScreenOn() {

        if (Build.VERSION.SDK_INT >= 27) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

    @Composable
    private fun AlarmExpandScreenContent(
        alarmFullScreenViewModel: AlarmFullScreenViewModel,
        digitalTime: Long
    ) {
        val alarmData by alarmFullScreenViewModel.alarmData.observeAsState()
        val context = LocalContext.current

        AlarmExpandedScreen(
            alarmLabel = alarmData?.alarmLabel ?: "",
            digitalTime = digitalTime,
            onSnoozeClick = {
                snoozeAlarm(alarmData!!)
            },
            onDismissClick = {
                dismissAlarm(alarmData!!)
            }
        )
    }

    private fun dismissAlarm(alarmData: AlarmMaster) {

        val intent = Intent(this@AlarmFullScreenActivity, AlarmReceiver::class.java)
        intent.putExtra(Constants.KEY_ALARM_ID, alarmData.alarmId)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmData.alarmDateInMillis)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmData.alarmTimeInMillis)
        intent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmData.alarmTriggerMillis)
        intent.putExtra(Constants.KEY_ALARM_LABEL, alarmData.alarmLabel)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmData.alarmType.name)
        intent.putExtra(Constants.KEY_ALARM_ACTION_TYPE, AlarmActionType.STOP.name)
        intent.putExtra(Constants.KEY_ALARM_TYPE, AlarmType.NA)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmData.alarmScheduledDays)
        intent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, alarmData.isAlarmVibrate)
        sendBroadcast(intent)
        closeActivity()

    }

    private fun closeActivity() {
        if (!this@AlarmFullScreenActivity.isDestroyed && !this@AlarmFullScreenActivity.isFinishing) {
            finish()
        }
    }

    private fun snoozeAlarm(alarmData: AlarmMaster) {

        val intent = Intent(this@AlarmFullScreenActivity, AlarmReceiver::class.java)
        intent.putExtra(Constants.KEY_ALARM_ID, alarmData.alarmId)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, alarmData.alarmDateInMillis)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, alarmData.alarmTimeInMillis)
        intent.putExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, alarmData.alarmTriggerMillis)
        intent.putExtra(Constants.KEY_ALARM_LABEL, alarmData.alarmLabel)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_TYPE, alarmData.alarmType.name)
        intent.putExtra(Constants.KEY_ALARM_ACTION_TYPE, AlarmActionType.SNOOZE.name)
        intent.putExtra(Constants.KEY_ALARM_TYPE, AlarmType.NA)
        intent.putExtra(Constants.KEY_ALARM_SCHEDULE_DAYS, alarmData.alarmScheduledDays)
        intent.putExtra(Constants.KEY_IS_ALARM_VIBRATE, alarmData.isAlarmVibrate)
        sendBroadcast(intent)
        closeActivity()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        updateAlarmData(intent)
    }

    private fun updateAlarmData(intent: Intent?) {
        if (intent != null) {
            val alarmId = intent.getIntExtra(Constants.KEY_ALARM_ID, -1)
            val alarmLabel = intent.getStringExtra(Constants.KEY_ALARM_LABEL) ?: ""
            val isAlarmVibrate = intent.getBooleanExtra(Constants.KEY_IS_ALARM_VIBRATE, false)
            val alarmScheduleDays = intent.getStringExtra(Constants.KEY_ALARM_SCHEDULE_DAYS) ?: ""
            val alarmScheduleTypeStr =
                intent.getStringExtra(Constants.KEY_ALARM_SCHEDULE_TYPE) ?: ""
            val alarmScheduleType = getAlarmScheduleType(alarmScheduleTypeStr)
            val alarmDateMillis = intent.getLongExtra(Constants.KEY_ALARM_SCHEDULE_DATE_MILLIS, -1L)
            val alarmTimeMillis = intent.getLongExtra(Constants.KEY_ALARM_SCHEDULE_TIME_MILLIS, -1L)
            val alarmTriggerMillis = intent.getLongExtra(Constants.KEY_ALARM_TRIGGER_MILLIS, -1L)

            val alarmMaster = AlarmMaster(
                alarmId,
                AlarmStatus.ON,
                alarmLabel,
                alarmTimeMillis,
                alarmDateMillis,
                alarmTriggerMillis,
                alarmScheduleDays,
                alarmScheduleType,
                isAlarmVibrate,
                0,
                false,
                0,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            )

            alarmFullScreenViewModel.updateAlarmData(alarmMaster)

        }
    }

    override fun onBackPressed() {
        //Restrict Back press
    }

}
