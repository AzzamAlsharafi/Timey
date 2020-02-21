package com.azzam.timey.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.azzam.timey.data.entity.Occurrence

@Dao
interface OccurrenceDao {
    @Query("SELECT * FROM occurrences_table")
    fun getAll(): LiveData<List<Occurrence>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(occurrences: List<Occurrence>): List<Long>

    @Delete
    suspend fun delete(occurrence: Occurrence)

    @Query("DELETE FROM occurrences_table WHERE parent_id == :parentId")
    suspend fun deleteAll(parentId: Int)
}