package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

@Entity(tableName = "tasks_table")
data class Task(
    var title: String,

    var description: String,

    @ColumnInfo(name = "date_time")
    var dateTime: OffsetDateTime,

    var timezone: ZoneId,

    @Embedded
    var repeating: Repeating,

    @Embedded
    var reminder: Reminder,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)