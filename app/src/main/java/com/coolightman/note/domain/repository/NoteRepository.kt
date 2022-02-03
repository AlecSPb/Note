package com.coolightman.note.domain.repository

import androidx.lifecycle.LiveData
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy

interface NoteRepository {

    suspend fun insertNote(note: Note)
    fun getAllNotes(sortBy: SortNoteBy): LiveData<List<Note>>
    suspend fun getNote(noteId: Long): Note
    suspend fun deleteNote(noteId: Long)
}