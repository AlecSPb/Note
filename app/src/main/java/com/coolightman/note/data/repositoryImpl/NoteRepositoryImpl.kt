package com.coolightman.note.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.coolightman.note.data.database.dao.NoteDao
import com.coolightman.note.data.mapper.toDb
import com.coolightman.note.data.mapper.toEntity
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: NoteDao
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        val noteDb = note.toDb()
        database.insert(noteDb)
    }

    override fun getAllNotes(): LiveData<List<Note>> {
        return Transformations.map(database.getAll()){ list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun getNote(noteId: Long): Note {
        return database.get(noteId).toEntity()
    }

    override suspend fun deleteNote(noteId: Long) {
        database.delete(noteId)
    }
}