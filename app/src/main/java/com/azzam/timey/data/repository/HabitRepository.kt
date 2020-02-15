package com.azzam.timey.data.repository

import androidx.lifecycle.LiveData
import com.azzam.timey.data.dao.HabitDao
import com.azzam.timey.data.entity.Habit

class HabitRepository private constructor(private val habitDao: HabitDao) {
    val allHabits: LiveData<List<Habit>> = habitDao.getAll()

    suspend fun insert(habit: Habit): Long = habitDao.insert(habit)

    suspend fun delete(habit: Habit) = habitDao.delete(habit)

    suspend fun update(habit: Habit) = habitDao.update(habit)

    companion object {
        @Volatile
        private var INSTANCE: HabitRepository? = null

        fun getInstance(dao: HabitDao): HabitRepository{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = HabitRepository(dao)
                INSTANCE = instance
                return instance
            }
        }
    }
}