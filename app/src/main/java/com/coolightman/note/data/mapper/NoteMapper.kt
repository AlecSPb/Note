package com.coolightman.note.data.mapper

import com.coolightman.note.data.database.dbModel.NoteDb
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.util.toDateString
import com.coolightman.note.util.toFullDateString

fun NoteDb.toEntity(): Note = Note(
    noteId = this.noteId,
    title = this.title,
    description = this.description.orEmpty(),
    dateEdit = this.dateEdit.toFullDateString(),
    color = this.color,
    isShowingDate = this.isShowingDate
)

fun Note.toDb(): NoteDb = NoteDb(
    noteId = this.noteId,
    title = this.title,
    description = this.description,
    color = this.color,
    isShowingDate = this.isShowingDate
)