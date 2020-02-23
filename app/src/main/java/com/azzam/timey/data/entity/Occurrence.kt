package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "occurrences_table")
data class Occurrence(
    @ColumnInfo(name = "parent_id")
    var parentId: Int,

    @ColumnInfo(name = "parent_type")
    var parentType: Int,

    @ColumnInfo(name = "start_date_time")
    var startDateTime: Date,

    @ColumnInfo(name = "end_date_time")
    var endDateTime: Date,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) {
    companion object {
        val PARENT_EVENT: Int = 0
        val PARENT_TASK: Int = 1
        val PARENT_HABIT: Int = 2
    }
}