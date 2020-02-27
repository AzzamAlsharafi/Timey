package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.ZonedDateTime

@Entity(tableName = "events_table")
data class Event(
    var title: String,

    var description: String,

    @ColumnInfo(name = "start_date_time")
    var startDateTime: ZonedDateTime,

    @ColumnInfo(name = "end_date_time")
    var endDateTime: ZonedDateTime,

    @ColumnInfo(name = "all_day")
    var allDay: Boolean,

    @Embedded
    var repeating: Repeating,

    @Embedded
    var reminder: Reminder,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)