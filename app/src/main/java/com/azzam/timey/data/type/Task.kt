package com.azzam.timey.data.type

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks_table")
data class Task(
    var title: String,

    var description: String,

    @ColumnInfo(name = "date_time")
    var dateTime: Date,

    @Embedded
    var repeating: Repeating?,

    @Embedded
    var reminder: Reminder?,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)