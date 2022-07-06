package com.coolightman.note.data.mapper

import com.coolightman.note.data.database.dbModel.NoteDb
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.util.toFullDateString
import java.util.*

fun NoteDb.toEntity(): Note = Note(
    noteId = this.noteId,
    title = this.title,
    description = this.description.orEmpty(),
    dateEdit = this.dateEdit.toFullDateString(),
    color = this.color,
    isShowingDate = this.isShowingDate,
    isEdited = this.isEdited,
    isDeleted = this.isDeleted
)

fun Note.toDb(): NoteDb = NoteDb(
    noteId = this.noteId,
    title = this.title,
    description = this.description,
    dateEdit = Date(System.currentTimeMillis()),
    color = this.color,
    isShowingDate = this.isShowingDate,
    isEdited = this.isEdited,
    isDeleted = this.isDeleted
)