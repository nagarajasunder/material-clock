package com.geekydroid.materialclock.ui.timer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.geekydroid.materialclock.application.constants.Constants
import com.geekydroid.materialclock.application.services.TimerService
import com.geekydroid.materialclock.application.utils.TimerLogicHandler
import com.geekydroid.materialclock.ui.theme.McAppTheme
import com.geekydroid.materialclock.ui.timer.composables.TimerCard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimerFullScreenActivity : ComponentActivity() {

    @Inject
    lateinit var timerLogicHandler: TimerLogicHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        turnScreenOn()
        setContent {
            McAppTheme {
                val timerEvent by timerLogicHandler.timerEvent.collectAsStateWithLifecycle()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        TimerCard(
                            modifier = Modifier
                                .padding(16.dp),
                            timerLabel = timerEvent.timerLabel,
                            timerText = timerEvent.timerText,
                            timerProgress = timerEvent.timerProgress,
                            timerState = timerEvent.timerState,
                            onAddOneMinuteClicked = {
                                addTime()
                            },
                            onPauseTimerClicked = { },
                            onResumeClicked = { },
                            onCloseTimerCLicked = {
                                stopTimer()
                            },
                            onResetTimerClicked = {
                                stopTimer()
                            })
                    }
                }
            }
        }
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

    private fun addTime() {
        val intent = Intent(this, TimerService::class.java)
        intent.putExtra(Constants.TIMER_ACTION_TYPE,Constants.TIMER_ACTION_ADD_MIN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        }
        else {
            startService(intent)
        }
        finish()
    }

    private fun stopTimer() {
        val intent = Intent(this, TimerService::class.java)
        intent.putExtra(Constants.TIMER_ACTION_TYPE,Constants.TIMER_ACTION_RESET_TIMER)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        }
        else {
            startService(intent)
        }
        finish()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    McAppTheme {
        Greeting("Android")
    }
}