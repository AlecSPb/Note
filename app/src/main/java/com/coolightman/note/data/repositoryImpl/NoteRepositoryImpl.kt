package com.coolightman.note.data.repositoryImpl

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.os.Environment
import com.coolightman.note.data.converter.convertJsonToNotes
import com.coolightman.note.data.converter.toJson
import com.coolightman.note.data.database.dao.NoteDao
import com.coolightman.note.data.mapper.toDb
import com.coolightman.note.data.mapper.toEntity
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.domain.repository.NoteRepository
import com.coolightman.note.util.SavedFileNameConstant.SAVE_FILES_LOCATION
import com.coolightman.note.util.SavedFileNameConstant.SAVE_FILE_NAME_NOTES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val database: NoteDao,
    private val context: Context
) : NoteRepository {

    override suspend fun insertNote(note: Note) {
        val noteDb = note.toDb()
        database.insert(noteDb)
    }

    override fun getAllNotes(sortBy: SortNoteBy): Flow<List<Note>> {
        return database.getAllOrderBy(sortBy.ordinal).map { list ->
            list.map { it.toEntity() }
        }
    }

    override fun getTrashNotes(): Flow<List<Note>> {
        return database.getTrash().map { list ->
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
        database.insertImportList(changedList)
    }

    override suspend fun sendToTrashBasket(noteId: Long) {
        val note = database.get(noteId)
        val trashedNote = note.copy(isDeleted = true)
        database.insert(trashedNote)
    }

    override fun getTrashCount(): Flow<Int> = database.countTrash()

    override suspend fun deleteAllPermanent() {
        database.deleteAllTrashPermanent()
    }

    override suspend fun restoreAllFromTrash() {
        val listOfTrash = database.getTrashList()
        val restoredList = listOfTrash.map { noteDb ->
            noteDb.copy(isDeleted = false)
        }
        database.insertList(restoredList)
    }

    override suspend fun deletePermanent(noteId: Long) {
        database.deletePermanent(noteId)
    }

    override suspend fun restoreFromTrash(noteId: Long) {
        val note = database.get(noteId)
        val restoredNote = note.copy(isDeleted = false)
        database.insert(restoredNote)
    }

    override suspend fun exportNotes() {
        val notesDb = database.getAll()
        val notesJson = notesDb.toJson()
        writeNotesInFile(notesJson)
    }

    private fun writeNotesInFile(notesJson: String) {
        val path = getPath()
        if (!path.exists()) {
            path.mkdir()
        }
        val file = File(path, SAVE_FILE_NAME_NOTES)

        if (file.exists()) {
            file.delete()
        }
        file.appendText(notesJson)

        writeFileToFolder(file)
    }

    private fun writeFileToFolder(file: File) {
        val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.addCompletedDownload(
            file.name,
            file.name,
            true,
            "text/plain",
            file.absolutePath,
            file.length(),
            true
        )
    }

    private fun getPath() = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        SAVE_FILES_LOCATION
    )

    override suspend fun importNotes() {
        val json = getJsonFromFolder()
        val notesDb = convertJsonToNotes(json)
        database.insertList(notesDb)
    }

    private fun getJsonFromFolder(): String {
        val file = File(getPath(), SAVE_FILE_NAME_NOTES)
        if (file.exists()) {
            return FileInputStream(file).bufferedReader().use { it.readText() }
        }
        throw IOException("File with notes is not exist!")
    }
}