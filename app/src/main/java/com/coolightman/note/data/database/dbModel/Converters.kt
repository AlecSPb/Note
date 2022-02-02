package com.coolightman.note.data.database.dbModel

import androidx.room.TypeConverter
import com.coolightman.note.domain.entity.NoteColor
import java.util.*

class Converters {

    @TypeConverter
    fun toNoteColor(color: String): NoteColor {
        return NoteColor.valueOf(color)
    }

    @TypeConverter
    fun fromNoteColor(color: NoteColor): String {
        return color.name
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

}