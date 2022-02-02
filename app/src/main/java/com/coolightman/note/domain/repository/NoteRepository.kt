package com.coolightman.note.domain.repository

import androidx.lifecycle.LiveData
import com.coolightman.note.domain.entity.Note

interface NoteRepository {

    suspend fun insertNote(note: Note)
    fun getAllNotes(): LiveData<List<Note>>
    suspend fun getNote(noteId: Long): Note
    suspend fun deleteNote(noteId: Long)
}