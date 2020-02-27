package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Task

@Dao
interface TaskDao {
    // TODO: Change Query clause to work with ThreeTen date and time objects
    @Query("SELECT * FROM tasks_table ORDER BY date_time")
    fun getAll(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task): Long

    @Delete
    suspend fun delete(task: Task)

    @Update
    suspend fun update(task: Task)
}