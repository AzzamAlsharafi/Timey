package com.azzam.timey.data

import androidx.room.TypeConverter
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.OffsetTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

// Data converter functions to convert between data types which can't be stored in the database.
class DataConverters {
    private val localTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun localTimeListToString(times: List<LocalTime>) : String{
        return times.joinToString(", ") { it.format(localTimeFormatter) }
    }

    @TypeConverter
    fun stringToLocalTimeList(string: String) : List<LocalTime>{
        return string.split(", ").map { LocalTime.parse(it, localTimeFormatter) }
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