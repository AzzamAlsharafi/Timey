package com.azzam.timey.data.type

import java.util.Date

data class Task(
    var title: String,
    var description: String,
    var dateTime: Date,
    var repeating: Repeating?,
    var reminder: Reminder?
)