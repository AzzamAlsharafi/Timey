package com.azzam.timey.data

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

// Data converter functions to convert between data types which can't be stored in the database.
class DataConverters {
    private val offsetDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val offsetTimeFormatter = DateTimeFormatter.ISO_OFFSET_TIME

    @TypeConverter
    fun stringToOffsetDateTime(string: String): OffsetDateTime {
        return OffsetDateTime.parse(string, offsetDateTimeFormatter)
    }

    @TypeConverter
    fun offsetDateTimeToString(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.format(offsetDateTimeFormatter)
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
    fun offsetTimeListToString(times: List<OffsetTime>) : String{
        return times.joinToString(", ") { it.format(offsetTimeFormatter) }
    }

    @TypeConverter
    fun stringToOffsetTimeList(string: String) : List<OffsetTime>{
        return string.split(", ").map { OffsetTime.parse(it, offsetTimeFormatter) }
    }

    @TypeConverter
    fun stringToZoneId(string: String): ZoneId {
        return ZoneId.of(string)
    }

    @TypeConverter
    fun zoneIdToString(zoneId: ZoneId): String {
        return zoneId.id
    }
}