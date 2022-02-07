package com.coolightman.note.di

import com.coolightman.note.data.repositoryImpl.NoteRepositoryImpl
import com.coolightman.note.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryBindModule {

    @Singleton
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
}