package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId

@Entity(tableName = "habits_table")
data class Habit(
    var title: String,

    var description: String,

    @ColumnInfo(name = "start_date")
    var startDate: LocalDate,

    @ColumnInfo(name = "end_date")
    var endDate: LocalDate,

    var times: List<OffsetTime>,

    var timezone: ZoneId,

    @Embedded
    var repeating: Repeating,

    @Embedded
    var reminder: Reminder,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)