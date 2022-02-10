package com.coolightman.note.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.coolightman.note.data.database.dbModel.NoteDb
import com.coolightman.note.data.database.dbModel.TaskDb

@Dao
interface TaskDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg task: TaskDb)

    @Insert(onConflict = REPLACE)
    suspend fun insertList(list: List<TaskDb>)

    @Query("SELECT * FROM taskdb WHERE taskId = :taskId")
    suspend fun getTask(taskId: Long): TaskDb

    @Query("SELECT * FROM taskdb")
    fun getAllOrderByDate(): LiveData<List<TaskDb>>

    @Query("SELECT COUNT(taskId) FROM taskdb")
    fun countNotes(): LiveData<Int>

    @Query("DELETE FROM taskdb WHERE taskId = :taskId")
    suspend fun delete(taskId: Long)
}