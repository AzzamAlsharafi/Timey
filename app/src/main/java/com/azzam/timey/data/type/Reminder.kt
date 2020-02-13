package com.azzam.timey.data.type

// Reminder class, which describes when to remind the user of an Event, Task, or Habit relative to the object's time.
data class Reminder(
    var value: Int, // Amount of time to remind the user relative to the actual time. Example: 15 (unit) before the time of the task.
    var unit: Int // Unit of time (check companion object below).
) {
    companion object Units{
        val MINUT: Int = 0
        val HOURS: Int = 1
        val DAYS: Int = 2
    }
}