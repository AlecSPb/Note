package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
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

    val trash: LiveData<List<Note>> = liveData {
        val trashNotes = repository.getTrashNotes()
        emitSource(trashNotes)
    }

    fun deleteAllPermanent() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.deleteAllPermanent()
        }
    }

    fun deletePermanent(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.deletePermanent(noteId)
        }
    }

    fun restoreAll() {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.restoreAllFromTrash()
        }
    }

    fun restoreFromTrash(noteId: Long) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            repository.restoreFromTrash(noteId)
        }
    }
}