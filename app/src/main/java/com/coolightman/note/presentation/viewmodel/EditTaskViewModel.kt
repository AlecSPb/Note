package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.Task
import com.coolightman.note.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    fun fetchTask(taskId: Long) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _task.postValue(repository.getTask(taskId))
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.insertTask(task)
        }
    }

}