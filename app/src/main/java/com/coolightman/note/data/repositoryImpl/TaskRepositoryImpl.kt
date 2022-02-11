package com.coolightman.note.data.repositoryImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.coolightman.note.data.database.dao.TaskDao
import com.coolightman.note.data.database.dbModel.TaskDb
import com.coolightman.note.data.mapper.toDb
import com.coolightman.note.data.mapper.toEntity
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.repository.TaskRepository
import com.coolightman.note.util.chainData
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val database: TaskDao
) : TaskRepository {

    override suspend fun insertTask(task: Task) {
        val taskDb = task.toDb()
        database.insert(taskDb)
    }

    override fun getAllTasks(): LiveData<List<Task>> {
        val liveDataTasks1 = database.getAllActiveImportant()
        val liveDataTasks2 = database.getAllActiveNotImportant()
        val liveDataTasks3 = database.getAllNotActiveImportant()
        val liveDataTasks4 = database.getAllNotActiveNotImportant()

        val merger = MediatorLiveData<List<TaskDb>>().chainData(
            liveDataTasks1,
            liveDataTasks2,
            liveDataTasks3,
            liveDataTasks4
        )
        return Transformations.map(merger) { list ->
            list.map { it.toEntity() }
        }
    }

    override suspend fun deleteTask(taskId: Long) {
        database.delete(taskId)
    }

    override suspend fun switchActive(taskId: Long) {
        val task = database.getTask(taskId)
        val editedTask = task.copy(isActive = !task.isActive)
        database.insert(editedTask)
    }

    override suspend fun getTask(taskId: Long): Task {
        return database.getTask(taskId).toEntity()
    }
}