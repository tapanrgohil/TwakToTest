package com.tapan.twaktotest.data.db

import androidx.room.TypeConverter
import java.util.*

class DateTimeConverter {

    @TypeConverter
    fun fromDateTime(dateTime: Date): Long {
        return dateTime.time
    }

    @TypeConverter
    fun toDateTime(millis: Long): Date {
        return Date(millis)
    }

}