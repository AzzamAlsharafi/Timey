package com.azzam.timey.data.entity

import androidx.room.ColumnInfo

// Repeating class, which describes how an Event, Task, or Habit repeats.
data class Repeating(
    @ColumnInfo(name = "repeating_type")
    var repeatingType: Int,

    @ColumnInfo(name = "repeating_value")
    var repeatingValue: Int, // value = 5: repeat once every 5 days.

    @ColumnInfo(name = "repeating_extra")
    var repeatingExtra: String, // Additional information depending on repeating type. Example: April 14 for YEARLY type.

    @ColumnInfo(name = "end_type")
    var endType: Int,

    @ColumnInfo(name = "end_extra")
    var endExtra: Long // Additional information depending on end type. Example: April 14 for DATE type.
) {
   companion object {
       // Repeating types
       val DAILY: Int = 0
       val WEEKLY: Int = 1
       val MONTHLY_BY_DAY: Int = 2 // Example: 10th of every month
       val MONTHLY_BY_DAY_OF_WEEK: Int = 3 // Example: Second Sunday
       val YEARLY: Int = 4

       // End types
       val NEVER: Int = 0 // Never stop repeating.
       val DATE: Int = 1 // Stop at a specific date.
       val OCCURRENCES: Int = 2 // Stop after a number of occurrences.

       val NO_REPEATING = Repeating(0, 0, "", 0, 0)
   }
}