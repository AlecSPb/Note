package com.coolightman.note.presentation.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.repository.NoteRepository

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    val notes: LiveData<List<Note>> = liveData {
        emitSource(repository.getAllNotes())
    }
}