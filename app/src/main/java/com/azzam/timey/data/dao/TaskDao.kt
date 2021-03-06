package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks_table ORDER BY date_time")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE id == :id")
    suspend fun getById(id: Int): Task

    @Query("SELECT * FROM tasks_table WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Int>): List<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task): Long

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)
}