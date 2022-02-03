package com.coolightman.note.presentation.viewmodel

import androidx.lifecycle.*
import com.coolightman.note.domain.entity.Note
import com.coolightman.note.domain.entity.SortNoteBy
import com.coolightman.note.domain.repository.NoteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _sortNoteBy = MutableLiveData<SortNoteBy>()

    val notes: LiveData<List<Note>> = liveData {
        val sortedNotes = Transformations.switchMap(_sortNoteBy) {
            repository.getAllNotes(it)
        }
        emitSource(sortedNotes)
    }

    fun setSortBy(sortBy: SortNoteBy) {
        _sortNoteBy.postValue(sortBy)
    }

    fun showDate(showDate: Boolean) {
        viewModelScope.launch {
            repository.showDate(showDate)
        }

    }
}