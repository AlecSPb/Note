package com.coolightman.note.data.database

import androidx.room.TypeConverter
import com.coolightman.note.domain.entity.NoteColor
import com.coolightman.note.domain.entity.TaskColor
import java.util.*

class Converters {

    @TypeConverter
    fun toNoteColor(color: Int): NoteColor {
        return NoteColor.values()[color]
    }

    @TypeConverter
    fun fromNoteColor(color: NoteColor): Int {
        return color.ordinal
    }

    @TypeConverter
    fun toTaskColor(color: Int): TaskColor {
        return TaskColor.values()[color]
    }

    @TypeConverter
    fun fromTaskColor(color: TaskColor): Int {
        return color.ordinal
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