package com.coolightman.note.di

import androidx.lifecycle.ViewModel
import com.coolightman.note.presentation.viewmodel.EditNoteViewModel
import com.coolightman.note.presentation.viewmodel.NotesTrashViewModel
import com.coolightman.note.presentation.viewmodel.NotesViewModel
import com.coolightman.note.presentation.viewmodel.TasksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(NotesViewModel::class)
    @Binds
    fun bindNotesViewModel(viewModel: NotesViewModel): ViewModel

    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    @Binds
    fun bindTasksViewModel(viewModel: TasksViewModel): ViewModel

    @IntoMap
    @ViewModelKey(EditNoteViewModel::class)
    @Binds
    fun bindEditNoteViewModel(viewModel: EditNoteViewModel): ViewModel

    @IntoMap
    @ViewModelKey(NotesTrashViewModel::class)
    @Binds
    fun bindNotesTrashViewModel(viewModel: NotesTrashViewModel): ViewModel
}