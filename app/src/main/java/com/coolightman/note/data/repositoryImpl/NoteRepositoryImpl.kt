package com.coolightman.note.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.coolightman.note.data.database.dao.NoteDao
import com.coolightman.note.data.mapper.toDb
import com.coolightman.note.data.mapper.toEntity
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.domain.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: NoteDao
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        val noteDb = note.toDb()
        database.insert(noteDb)
    }

    override fun getAllNotes(sortBy: SortNoteBy): LiveData<List<Note>> {
        when (sortBy) {
            SortNoteBy.COLOR -> {
                return Transformations.map(database.getAllOrderByColor()) { list ->
                    list.map { it.toEntity() }
                }
            }
            SortNoteBy.COLOR_DESC -> {
                return Transformations.map(database.getAllOrderByColorDesc()) { list ->
                    list.map { it.toEntity() }
                }
            }
            SortNoteBy.DATE -> {
                return Transformations.map(database.getAllOrderByDate()) { list ->
                    list.map { it.toEntity() }
                }
            }
            SortNoteBy.DATE_DESC -> {
                return Transformations.map(database.getAllOrderByDateDesc()) { list ->
                    list.map { it.toEntity() }
                }
            }
            SortNoteBy.DATE_EDIT -> {
                return Transformations.map(database.getAllOrderByEditDate()) { list ->
                    list.map { it.toEntity() }
                }
            }
            SortNoteBy.DATE_EDIT_DESC -> {
                return Transformations.map(database.getAllOrderByEditDateDesc()) { list ->
                    list.map { it.toEntity() }
                }
            }
        }

    }

    override fun getTrashNotes(): LiveData<List<Note>> {
        return Transformations.map(database.getTrash()) { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun getNote(noteId: Long): Note {
        return database.get(noteId).toEntity()
    }

    override suspend fun deleteNote(noteId: Long) {
        database.delete(noteId)
    }

    override suspend fun showDate(showDate: Boolean) {
        val changedList = database.getAll().map { noteDb ->
            noteDb.copy(isShowingDate = showDate)
        }
        database.insertList(changedList)
    }

    override suspend fun sendToTrashBasket(noteId: Long) {
        val note = database.get(noteId)
        val trashedNote = note.copy(isDeleted = true)
        database.insert(trashedNote)
    }

    override fun getTrashCount(): LiveData<Int> = database.countTrash()

    override suspend fun deleteAllTrash() {
        database.deleteTrashPermanent()
    }

    override suspend fun restoreAllFromTrash() {
        withContext(Dispatchers.IO){
            val listOfTrash = database.getTrashList()
            val restoredList = listOfTrash.map { noteDb ->
                noteDb.copy(isDeleted = false)
            }
            database.insertList(restoredList)
        }
    }
}