package com.coolightman.note.di

import com.coolightman.note.data.repositoryImpl.NoteRepositoryImpl
import com.coolightman.note.data.repositoryImpl.TaskRepositoryImpl
import com.coolightman.note.domain.repository.NoteRepository
import com.coolightman.note.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryBindModule {

    @Singleton
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Singleton
    @Binds
    fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository
}