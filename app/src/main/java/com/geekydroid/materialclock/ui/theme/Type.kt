package com.geekydroid.materialclock.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.geekydroid.materialclock.R

val Oswald = FontFamily(
    Font(R.font.oswald_semibold)
)

val timerTextStyle = TextStyle(
    fontFamily = Oswald,
    fontWeight = FontWeight.Light,
    fontSize = 60.sp,
    letterSpacing = 2.sp
)