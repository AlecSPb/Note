package com.coolightman.note.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.domain.repository.NoteRepository
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _sortNoteBy = MutableLiveData<SortNoteBy>()

    val notes: LiveData<List<Note>> = liveData {
        val sort = _sortNoteBy.value ?: SortNoteBy.COLOR
        emitSource(repository.getAllNotes(sort))
    }

    fun setSortBy(sortBy: SortNoteBy){
        _sortNoteBy.postValue(sortBy)
    }
}