package com.geekydroid.materialclock.application.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geekydroid.materialclock.application.db.dao.AlarmDao
import com.geekydroid.materialclock.application.db.dao.ScheduledAlarmDao
import com.geekydroid.materialclock.ui.alarm.model.AlarmMaster
import com.geekydroid.materialclock.ui.alarm.model.ScheduledAlarm

@Database(entities = [AlarmMaster::class,ScheduledAlarm::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    abstract fun scheduledAlarmDao() : ScheduledAlarmDao
}