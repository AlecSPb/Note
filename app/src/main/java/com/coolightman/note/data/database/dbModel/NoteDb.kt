package com.coolightman.note.data.database.dbModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coolightman.note.domain.entity.NoteColor
import java.util.*

@Entity
data class NoteDb(
    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,
    val title: String,
    val description: String?,
    val dateEdit: Date = Date(System.currentTimeMillis()),
    val color: NoteColor,
    val isShowingDate: Boolean
)
