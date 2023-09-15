package com.geekydroid.materialclock.ui.alarm.model

import androidx.annotation.RawRes

data class AlarmSound(
    val soundId:Int,
    @RawRes val soundFile:Int,
    val soundLabel:String
)