package com.azzam.timey.data.type

import java.util.Date

data class Habit(
    var title: String,
    var description: String,
    var startDate: Date,
    var finishDate: Date?,
    var times: List<Date>,
    var repeating: Repeating?,
    var reminder: Reminder?
)