package com.coolightman.note.di

import androidx.lifecycle.ViewModel
import com.coolightman.note.presentation.viewmodel.*
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

    @IntoMap
    @ViewModelKey(EditTaskViewModel::class)
    @Binds
    fun bindEditTaskViewModel(viewModel: EditTaskViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    @Binds
    fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}