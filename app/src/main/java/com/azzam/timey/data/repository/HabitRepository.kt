package com.azzam.timey.data.repository

import androidx.lifecycle.LiveData
import com.azzam.timey.data.dao.HabitDao
import com.azzam.timey.data.entity.Habit

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: LiveData<List<Habit>> = habitDao.getAll()

    suspend fun getById(id: Int): Habit = habitDao.getById(id)

    suspend fun getByIds(ids: List<Int>): List<Habit> = habitDao.getByIds(ids)

    suspend fun insert(habit: Habit): Long = habitDao.insert(habit)

    suspend fun delete(habit: Habit) = habitDao.delete(habit)

    suspend fun update(habit: Habit) = habitDao.update(habit)
}