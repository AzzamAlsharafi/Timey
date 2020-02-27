package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Event

@Dao
interface EventDao {
    // TODO: Change Query clause to work with ThreeTen date and time objects
    @Query("SELECT * FROM events_table ORDER BY start_date_time")
    fun getAll(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event): Long

    @Delete
    suspend fun delete(event: Event)

    @Update
    suspend fun update(event: Event)
}