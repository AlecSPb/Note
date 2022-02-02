package com.coolightman.note.presentation.notes

import android.util.Log
import androidx.lifecycle.*
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Coroutine_exception", throwable.stackTraceToString())
    }

    val notes: LiveData<List<Note>> = liveData {
        emitSource(repository.getAllNotes())
    }
}