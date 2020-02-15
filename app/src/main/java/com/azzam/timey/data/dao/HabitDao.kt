package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.type.Habit

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits_table ORDER BY start_date")
    fun getAll(): LiveData<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(habit: Habit): Long

    @Delete
    suspend fun delete(habit: Habit)

    @Update
    suspend fun update(habit: Habit)
}