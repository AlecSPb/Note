package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

    private val ioContext = Dispatchers.IO + handler

    val tasks: LiveData<List<Task>> = repository.getAllTasks().asLiveData()
    private var deleteTaskJob: Job? = null

    fun switchActive(taskId: Long) {
        viewModelScope.launch(ioContext) {
            repository.switchActive(taskId)
        }
    }

    fun deleteTask(taskId: Long) {
        deleteTaskJob = viewModelScope.launch(ioContext) {
            repository.setTaskIsDeleted(taskId, true)
            delay(5000)
            repository.deleteTask(taskId)
        }
    }

    fun cancelDeleteTask(taskId: Long) {
        viewModelScope.launch(ioContext) {
            deleteTaskJob?.cancel()
            repository.setTaskIsDeleted(taskId, false)
        }
    }
}