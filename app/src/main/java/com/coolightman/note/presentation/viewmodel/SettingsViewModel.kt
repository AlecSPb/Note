package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.note.domain.repository.NoteRepository
import com.coolightman.note.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val taskRepository: TaskRepository
): ViewModel(){

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    fun exportData() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            noteRepository.exportNotes()
            taskRepository.exportTasks()
        }
    }

    fun importData() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            noteRepository.importNotes()
            taskRepository.importTasks()
        }
    }
}