package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId

@Entity(tableName = "habits_table")
data class Habit(
    var title: String,

    var description: String,

    @ColumnInfo(name = "start_date")
    var startDate: Long, // stored as milliseconds since epoch

    var times: List<LocalTime>,

    var timezone: ZoneId,

    @Embedded
    var repeating: Repeating,

    @Embedded
    var reminder: Reminder,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)