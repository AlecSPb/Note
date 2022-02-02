package com.coolightman.note.domain.entity

data class Note(
    val noteId: Long = 0,
    val title: String,
    val description: String,
    val dateEdit: String,
    val color: NoteColor,
    val isShowingDate: Boolean
)
