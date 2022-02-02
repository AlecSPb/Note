package com.coolightman.note.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note

    fun fetchNote(noteId: Long) {
        viewModelScope.launch(handler) {
            _note.postValue(repository.getNote(noteId))
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch(handler) {
            repository.insertNote(note)
        }
    }

}