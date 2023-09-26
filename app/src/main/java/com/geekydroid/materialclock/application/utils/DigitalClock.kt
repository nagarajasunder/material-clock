package com.geekydroid.materialclock.application.utils

import com.geekydroid.materialclock.application.di.ApplicationScope
import com.geekydroid.materialclock.application.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "DigitalClock"

