package com.azzam.timey.data

import androidx.room.TypeConverter
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

// Data converter functions to convert between data types which can't be stored in the database.
class DataConverters {
    private val zonedDateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    private val localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val localTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun stringToZonedDateTime(string: String): ZonedDateTime {
        return ZonedDateTime.parse(string, zonedDateTimeFormatter)
    }

    @TypeConverter
    fun zonedDateTimeToString(zonedDateTime: ZonedDateTime): String {
        return zonedDateTime.format(zonedDateTimeFormatter)
    }

    @TypeConverter
    fun stringToLocalDate(string: String): LocalDate {
        return LocalDate.parse(string, localDateFormatter)
    }

    @TypeConverter
    fun localDateToString(localDate: LocalDate): String {
        return localDate.format(localDateFormatter)
    }

    @TypeConverter
    fun localTimeListToString(times: List<LocalTime>) : String{
        return times.map { it.format(localTimeFormatter) }.joinToString(", ")
    }

    @TypeConverter
    fun stringToLocalTimeList(string: String) : List<LocalTime>{
        return string.split(", ").map { LocalTime.parse(it, localTimeFormatter) }
    }
}