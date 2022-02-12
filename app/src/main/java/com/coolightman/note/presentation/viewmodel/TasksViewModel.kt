package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.repository.TaskRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    val tasks: LiveData<List<Task>> = repository.getAllTasks()
    private var deleteTaskJob: Job? = null

    fun switchActive(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.switchActive(taskId)
        }
    }

    fun deleteTask(taskId: Long) {
        deleteTaskJob = viewModelScope.launch(Dispatchers.IO + handler) {
            repository.setTaskIsDeleted(taskId, true)
            delay(5000)
            repository.deleteTask(taskId)
        }
    }

    fun cancelDeleteTask(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            deleteTaskJob?.cancel()
            repository.setTaskIsDeleted(taskId, false)
        }
    }
}