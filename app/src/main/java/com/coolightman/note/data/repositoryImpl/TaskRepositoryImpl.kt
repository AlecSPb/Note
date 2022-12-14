package com.coolightman.note.data.repositoryImpl

import com.coolightman.note.data.converter.toJson
import com.coolightman.note.data.database.dao.TaskDao
import com.coolightman.note.data.mapper.toDb
import com.coolightman.note.data.mapper.toEntity
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val database: TaskDao
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        val taskDb = task.toDb()
        database.insert(taskDb)
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return database.getAllTasks().map { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun deleteTask(taskId: Long) {
        database.delete(taskId)
    }

    override suspend fun setTaskIsDeleted(taskId: Long, isDeleted: Boolean) {
        val task = database.getTask(taskId)
        val editedTask = task.copy(isDeleted = isDeleted)
        database.insert(editedTask)
    }

    override suspend fun switchActive(taskId: Long) {
        val task = database.getTask(taskId)
        val editedTask = task.copy(isActive = !task.isActive)
        database.insert(editedTask)
    }

    override suspend fun getTask(taskId: Long): Task {
        return database.getTask(taskId).toEntity()
    }

    override suspend fun deleteAllInactive() {
        database.deleteAllInactive()
    }

    override suspend fun exportTasks() {
        val tasksDb = database.getAll()
        val taskJson = tasksDb.toJson()
    }

    override suspend fun importTasks() {
        TODO("Not yet implemented")
    }
}