package com.azzam.timey.data.type

import java.util.Date

data class Event(
    var title: String,
    var description: String,
    var startDateTime: Date,
    var finishDateTime: Date,
    var allDay: Boolean,
    var repeating: Repeating?,
    var reminder: Reminder?
)