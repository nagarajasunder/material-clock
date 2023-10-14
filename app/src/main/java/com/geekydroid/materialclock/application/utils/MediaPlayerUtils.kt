package com.geekydroid.materialclock.application.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.util.Log
import androidx.annotation.RawRes
import com.geekydroid.materialclock.application.constants.Constants


object MediaPlayerUtils {

    private const val TAG = "MediaPlayerUtils"

    private var mediaPlayer: MediaPlayer? = null
    private var mediaType: MediaType? = null
    private var alarmSoundId:Int = -1

    private var isAlarmFired = false
    private var isTimerFired = false

    fun playSound(context: Context, id: Int, type:MediaType) {
        Log.d(TAG, "playSound: $type")
        stopIfPlaying()
        try {
            mediaType = type
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            when(type) {
                MediaType.TIMER -> {
                    isTimerFired = true
                    val timerSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    mediaPlayer?.setDataSource(context,timerSound)
                }
                MediaType.ALARM -> {
                    isAlarmFired = true
                    alarmSoundId = id
                    @RawRes val soundFile = Constants.alarmSoundsList[id].soundFile
                    context.resources.openRawResourceFd(soundFile)?.let {
                        mediaPlayer?.setDataSource(it.fileDescriptor,it.startOffset,it.length)
                    }
                }
            }
            mediaPlayer?.isLooping = true
            mediaPlayer?.setOnPreparedListener {
                it.start()
            }
            mediaPlayer?.prepareAsync()
        }
        catch (e:Exception) {
            Log.d(TAG, "playSound: ${e.localizedMessage}")
        }
    }

    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = null
        mediaType = null
    }

    private fun stopIfPlaying() {
        if(mediaPlayer?.isPlaying == true) {
           stopSound()
        }
    }

    fun stopAlarmSound(context: Context) {
        isAlarmFired = false
        stopSound()
        if(isTimerFired) {
            playSound(context,-1,MediaType.TIMER)
        }
    }

    fun stopTimerSound(context: Context) {
        isTimerFired = false
        stopSound()
        Log.d(TAG, "stopTimerSound: alarmfired $isAlarmFired")
        if(isAlarmFired) {
            playSound(context, alarmSoundId,MediaType.ALARM)
        }
    }
}

enum class MediaType {
    TIMER,
    ALARM
}