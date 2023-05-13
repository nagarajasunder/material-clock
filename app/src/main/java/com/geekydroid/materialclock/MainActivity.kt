package com.geekydroid.materialclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.geekydroid.materialclock.ui.theme.McAppTheme
import com.geekydroid.materialclock.ui.McApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            McAppTheme {
                McApp()
            }

        }
    }


}