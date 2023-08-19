package com.geekydroid.materialclock.ui.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.geekydroid.materialclock.ui.theme.McAppTheme

class AlarmExpandActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            McAppTheme {
                //AlarmExpandedScreen()
            }
        }
    }

}
