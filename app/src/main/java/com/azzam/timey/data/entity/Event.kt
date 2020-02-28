package com.azzam.timey.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

@Entity(tableName = "events_table")
data class Event(
    var title: String,

    var description: String,

    @ColumnInfo(name = "start_date_time")
    var startDateTime: OffsetDateTime,

    @ColumnInfo(name = "end_date_time")
    var endDateTime: OffsetDateTime,

    var timezone: ZoneId,

    @ColumnInfo(name = "all_day")
    var allDay: Boolean,

    @Embedded
    var repeating: Repeating,

    @Embedded
    var reminder: Reminder,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)