package com.coolightman.note.domain.repository

import com.coolightman.note.domain.entity.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insertTask(task: Task)
    fun getAllTasks(): Flow<List<Task>>
    suspend fun deleteTask(taskId: Long)
    suspend fun switchActive(taskId: Long)
    suspend fun getTask(taskId: Long): Task
    suspend fun deleteAllInactive()
    suspend fun setTaskIsDeleted(taskId: Long, isDeleted: Boolean)
    suspend fun exportTasks()
    suspend fun importTasks()
}