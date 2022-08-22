package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesTrashViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    private val ioContext = Dispatchers.IO + handler

    val trash: LiveData<List<Note>> = repository.getTrashNotes().asLiveData()

    fun deleteAllPermanent() {
        viewModelScope.launch(ioContext) {
            repository.deleteAllPermanent()
        }
    }

    fun deletePermanent(noteId: Long) {
        viewModelScope.launch(ioContext) {
            repository.deletePermanent(noteId)
        }
    }

    fun restoreAll() {
        viewModelScope.launch(ioContext) {
            repository.restoreAllFromTrash()
        }
    }

    fun restoreFromTrash(noteId: Long) {
        viewModelScope.launch(ioContext) {
            repository.restoreFromTrash(noteId)
        }
    }
}