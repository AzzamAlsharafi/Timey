package com.azzam.timey.data.entity

// Reminder class, which describes when to remind the user of an Event, Task, or Habit relative to the object's time.
data class Reminder(
    var reminderValue: Int, // Amount of time to remind the user relative to the actual time. Example: 15 minutes before the time of the task.
    var unit: Int
) {
    companion object {
        val MINUTES: Int = 0
        val HOURS: Int = 1
        val DAYS: Int = 2

        val ON_TIME_REMINDER = Reminder(0, 0)
        val NO_REMINDER = Reminder(0, 3)
    }
}