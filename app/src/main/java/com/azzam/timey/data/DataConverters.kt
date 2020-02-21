package com.azzam.timey.data

import androidx.room.TypeConverter
import java.util.*

// Data converter functions to convert between data types which can't be stored in the database.
class DataConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun dateListToString(dates: List<Date>) : String{
        return dates.joinToString(separator = ", ") { it.time.toString() }
    }

    @TypeConverter
    fun stringToDateList(string: String) : List<Date>{
        return string.split(", ").map { Date(it.toLong()) }
    }
}