package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.type.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM events_table ORDER BY start_date_time")
    suspend fun getAll(): List<Event>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event): Long

    @Delete
    suspend fun delete(event: Event)

    @Update
    suspend fun update(event: Event)
}