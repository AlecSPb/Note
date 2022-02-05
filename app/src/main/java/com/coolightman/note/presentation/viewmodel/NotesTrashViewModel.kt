package com.coolightman.note.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.repository.NoteRepository
import javax.inject.Inject

class NotesTrashViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {


    val trash: LiveData<List<Note>> = liveData {
        val trashNotes = repository.getTrashNotes()
        emitSource(trashNotes)
    }
}