package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "occurrences_table")
data class Occurrence(
    @ColumnInfo(name = "parent_id")
    var parentId: Int,

    @ColumnInfo(name = "start_date_time")
    var startDateTime: Date,

    @ColumnInfo(name = "end_date_time")
    var endDateTime: Date,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)