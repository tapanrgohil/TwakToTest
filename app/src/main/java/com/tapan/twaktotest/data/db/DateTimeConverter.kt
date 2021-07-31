package com.tapan.twaktotest.data.db

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class DateTimeConverter {

    @TypeConverter
    fun fromDateTime(dateTime: ZonedDateTime): Long {
        return dateTime.toEpochSecond()
    }

    @TypeConverter
    fun toDateTime(millis: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(millis), ZoneId.systemDefault())
    }

}