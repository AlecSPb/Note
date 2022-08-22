package com.coolightman.note.domain.repository

import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun insertNote(note: Note)
    fun getAllNotes(sortBy: SortNoteBy): Flow<List<Note>>
    fun getTrashNotes(): Flow<List<Note>>
    suspend fun getNote(noteId: Long): Note
    suspend fun deleteNote(noteId: Long)
    suspend fun showDate(showDate: Boolean)
    suspend fun sendToTrashBasket(noteId: Long)
    fun getTrashCount(): Flow<Int>
    suspend fun deleteAllPermanent()
    suspend fun restoreAllFromTrash()
    suspend fun deletePermanent(noteId: Long)
    suspend fun restoreFromTrash(noteId: Long)
    suspend fun exportNotes()
    suspend fun importNotes()
}