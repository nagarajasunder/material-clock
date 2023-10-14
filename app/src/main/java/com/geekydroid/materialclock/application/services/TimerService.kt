package com.geekydroid.materialclock.application.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import com.geekydroid.materialclock.application.notification.TimerNotificationHelper
import com.geekydroid.materialclock.application.utils.MediaPlayerUtils
import com.geekydroid.materialclock.application.utils.MediaType
import com.geekydroid.materialclock.application.utils.TimerLogicHandler
import com.geekydroid.materialclock.application.utils.TimerUtils
import com.geekydroid.materialclock.ui.timer.models.TimerEvent
import com.geekydroid.materialclock.ui.timer.models.TimerState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TimerService"

@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var timerLogicHandler: TimerLogicHandler

    @Inject
    @ApplicationScope
    lateinit var externalScope: CoroutineScope

    @Inject
    @IoDispatcher
    lateinit var externalDispatcher: CoroutineDispatcher

    private var timerSoundStarted = false


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timerActionType = intent?.getStringExtra(Constants.TIMER_ACTION_TYPE)
        if (timerActionType == Constants.TIMER_ACTION_START_TIMER) {
            val timerEvent = intent.getParcelableExtra<TimerEvent>(Constants.TIMER_EVENT_BUNDLE)
            if (timerEvent != null) {
                timerLogicHandler.startTimer(timerEvent)
                val timerNotification = TimerNotificationHelper.createTimerNotification(
                    this,
                    timerEvent,
                    timerEvent.timerText
                )
                startForeground(Constants.TIMER_NOTIFICATION_ID, timerNotification)
                observeTimerChanges()
            }
        } else if (timerActionType == Constants.TIMER_ACTION_PAUSE_TIMER) {
            timerLogicHandler.pauseTimer()
        } else if (timerActionType == Constants.TIMER_ACTION_ADD_MIN) {
            timerSoundStarted = false
            MediaPlayerUtils.stopTimerSound(this@TimerService)
            timerLogicHandler.addOneMinuteToTimer()
        } else if (timerActionType == Constants.TIMER_ACTION_RESUME_TIMER) {
            timerLogicHandler.resumeTimer()
        } else if (timerActionType == Constants.TIMER_ACTION_RESET_TIMER) {
            timerSoundStarted = false
            MediaPlayerUtils.stopTimerSound(this@TimerService)
            timerLogicHandler.closeTimer()
        }

        return START_STICKY

    }

    private fun observeTimerChanges() {
        externalScope.launch(externalDispatcher) {
            timerLogicHandler.timerEvent.collect { timerEvent ->
                val notificationTitle = TimerUtils.getTimerTextBasedOnMillisForNotification(timerEvent.currentTimerMillis)
                when (timerEvent.timerState) {
                    TimerState.IDLE, TimerState.RESET -> {
                        TimerNotificationHelper.removeTimerNotification(this@TimerService)
                        stopSelf()
                    }
                    TimerState.EXCEEDED -> {
                        if(!timerSoundStarted) {
                            MediaPlayerUtils.playSound(this@TimerService,-1,MediaType.TIMER)
                            timerSoundStarted = true
                        }
                        TimerNotificationHelper.postTimeUpNotification(this@TimerService, timerEvent.copy(timerText = notificationTitle))
                    }
                    else -> {
                        TimerNotificationHelper.updateTimerNotification(
                            this@TimerService,
                            timerEvent,
                            notificationTitle
                        )
                    }
                }
            }
        }
    }
}