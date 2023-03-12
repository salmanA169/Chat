package com.swalif.sa.datasource.local.convertor

import androidx.room.TypeConverter
import com.swalif.sa.utils.toSeconds
import com.swalif.sa.utils.toLocalDateTime
import java.time.LocalDateTime

class Convertor {
    @TypeConverter
    fun fromSecondsToLocalDateTime(date:Long):LocalDateTime{
        return date.toLocalDateTime()
    }

    @TypeConverter
    fun fromLocalDateTimeToSeconds(localDateTime: LocalDateTime):Long{
        return localDateTime.toSeconds()
    }
}