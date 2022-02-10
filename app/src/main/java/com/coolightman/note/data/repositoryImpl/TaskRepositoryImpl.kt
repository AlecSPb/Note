package com.coolightman.note.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.coolightman.note.data.database.dao.TaskDao
import com.coolightman.note.data.mapper.toDb
import com.coolightman.note.data.mapper.toEntity
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val database: TaskDao
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        val taskDb = task.toDb()
        database.insert(taskDb)
    }

    override fun getAllTasks(): LiveData<List<Task>> {
        return Transformations.map(database.getAllOrderByDate()) { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun deleteTask(taskId: Long) {
        database.delete(taskId)
    }

    override suspend fun switchActive(taskId: Long) {
        val task = database.get(taskId)
        val editedTask = task.copy(isActive = !task.isActive)
        database.insert(editedTask)
    }
}