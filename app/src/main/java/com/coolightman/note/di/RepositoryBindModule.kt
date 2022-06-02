package com.coolightman.note.di

import com.coolightman.note.data.repositoryImpl.NoteRepositoryImpl
import com.coolightman.note.data.repositoryImpl.TaskRepositoryImpl
import com.coolightman.note.domain.repository.NoteRepository
import com.coolightman.note.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryBindModule {

    @ApplicationScope
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @ApplicationScope
    @Binds
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}