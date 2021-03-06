package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Occurrence

@Dao
interface OccurrenceDao {
    @Query("SELECT * FROM occurrences_table WHERE (NOT :withReminder OR unit != 3) AND (start_date_time BETWEEN :rangeStart AND :rangeEnd)")
    fun getAllInTimeRange(rangeStart: Long, rangeEnd: Long, withReminder: Boolean): List<Occurrence>

    @Query("SELECT * FROM occurrences_table WHERE parent_id == :id AND parent_type == :type")
    fun getAllOfParent(id: Int, type: Int): LiveData<List<Occurrence>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(occurrences: List<Occurrence>): List<Long>

    @Delete
    suspend fun delete(occurrence: Occurrence)

    @Query("DELETE FROM occurrences_table WHERE parent_id == :parentId")
    suspend fun deleteAll(parentId: Int)
}