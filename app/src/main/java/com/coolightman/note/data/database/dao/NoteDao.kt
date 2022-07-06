package com.coolightman.note.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.coolightman.note.data.database.dbModel.NoteDb

@Dao
interface NoteDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg note: NoteDb)

    @Insert(onConflict = REPLACE)
    suspend fun insertList(list: List<NoteDb>)

    @Insert(onConflict = IGNORE)
    suspend fun insertImportList(list: List<NoteDb>)

    @Query("SELECT * FROM notedb WHERE noteId = :noteId")
    suspend fun get(noteId: Long): NoteDb

    @Query("SELECT * FROM notedb WHERE isDeleted = 0")
    suspend fun getAll(): List<NoteDb>

    @Query(
        "SELECT * FROM notedb WHERE isDeleted = 0 ORDER BY " +
                "CASE WHEN :sortType = 0 THEN color END ASC, " +
                "CASE WHEN :sortType = 1 THEN color END DESC, " +
                "CASE WHEN :sortType = 2 THEN dateEdit END ASC, " +
                "CASE WHEN :sortType = 3 THEN dateEdit END DESC, " +
                "CASE WHEN :sortType = 4 THEN noteId END ASC, " +
                "CASE WHEN :sortType = 5 THEN noteId END DESC"
    )
    fun getAllOrderBy(sortType: Int): LiveData<List<NoteDb>>

    @Query("SELECT * FROM notedb WHERE isDeleted = 1 ORDER BY dateEdit desc")
    fun getTrash(): LiveData<List<NoteDb>>

    @Query("SELECT * FROM notedb WHERE isDeleted = 1")
    fun getTrashList(): List<NoteDb>

    @Query("SELECT COUNT(noteId) FROM notedb WHERE isDeleted = 1")
    fun countTrash(): LiveData<Int>

    @Query("SELECT COUNT(noteId) FROM notedb WHERE isDeleted = 0")
    fun countNotes(): LiveData<Int>

    @Query("DELETE FROM notedb WHERE noteId = :noteId")
    suspend fun delete(noteId: Long)

    @Query("DELETE FROM notedb WHERE isDeleted = 1")
    suspend fun deleteAllTrashPermanent()

    @Query("DELETE FROM notedb WHERE noteId = :noteId")
    suspend fun deletePermanent(noteId: Long)
}