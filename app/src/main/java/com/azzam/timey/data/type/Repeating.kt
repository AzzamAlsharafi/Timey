package com.azzam.timey.data.type

// Repeating class, which describes how an Event, Task, or Habit repeats.
data class Repeating(
    var repeatingType: Int,
    var repeatingValue: Int, // value = 5: repeat once every 5 days.
    var repeatingExtra: String, // Additional information depending on repeating type. Example: April 14 for YEARLY type.
    var endType: Int,
    var endExtra: Long // Additional information depending on end type. Example: April 14 for DATE type.
) {
   companion object Types{
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
   }
}