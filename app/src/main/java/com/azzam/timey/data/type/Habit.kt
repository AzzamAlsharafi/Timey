package com.azzam.timey.data.type

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "habits_table")
data class Habit(
    var title: String,

    var description: String,

    @ColumnInfo(name = "start_date")
    var startDate: Date,

    var times: List<Date>,

    @Embedded
    var repeating: Repeating,

    @Embedded
    var reminder: Reminder?,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)