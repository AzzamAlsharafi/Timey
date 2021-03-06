package com.azzam.timey.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.azzam.timey.data.dao.EventDao
import com.azzam.timey.data.dao.HabitDao
import com.azzam.timey.data.dao.OccurrenceDao
import com.azzam.timey.data.dao.TaskDao
import com.azzam.timey.data.entity.Event
import com.azzam.timey.data.entity.Habit
import com.azzam.timey.data.entity.Occurrence
import com.azzam.timey.data.entity.Task

@Database(entities = arrayOf(Event::class, Habit::class, Task::class, Occurrence::class), version = 1, exportSchema = false)
@TypeConverters(DataConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun EventDao(): EventDao
    abstract fun HabitDao(): HabitDao
    abstract fun TaskDao(): TaskDao
    abstract fun OccurrenceDao(): OccurrenceDao

    companion object {

        // Singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}