package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits_table ORDER BY datetime(start_date)")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM habits_table WHERE id == :id")
    suspend fun getById(id: Int): Habit

    @Query("SELECT * FROM habits_table WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<Habit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit): Long

    @Delete
    suspend fun delete(habit: Habit)

    @Update
    suspend fun update(habit: Habit)
}