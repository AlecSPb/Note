package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    val tasks: LiveData<List<Task>> = liveData {
        emitSource(repository.getAllTasks())
    }

    fun switchActive(taskId: Long){
        viewModelScope.launch(Dispatchers.IO + handler){
            repository.switchActive(taskId)
        }
    }
}