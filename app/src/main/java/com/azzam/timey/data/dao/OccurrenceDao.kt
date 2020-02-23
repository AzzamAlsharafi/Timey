package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Occurrence

@Dao
interface OccurrenceDao {
    @Query("SELECT * FROM occurrences_table WHERE start_date_time <= :rangeEnd AND end_date_time >= :rangeStart")
    fun getAllInTimeRange(rangeStart: Long, rangeEnd: Long): LiveData<List<Occurrence>>

    @Query("SELECT * FROM occurrences_table WHERE parent_id == :id AND parent_type == :type")
    fun getAllOfParent(id: Int, type: Int): LiveData<List<Occurrence>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(occurrences: List<Occurrence>): List<Long>

    @Delete
    suspend fun delete(occurrence: Occurrence)

    @Query("DELETE FROM occurrences_table WHERE parent_id == :parentId")
    suspend fun deleteAll(parentId: Int)
}