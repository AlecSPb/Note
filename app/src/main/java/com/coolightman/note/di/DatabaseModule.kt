package com.coolightman.note.di

import android.content.Context
import com.coolightman.note.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(context: Context) = AppDatabase.getMovieDatabase(context)

    @Singleton
    @Provides
    fun provideNoteDao(db: AppDatabase) = db.noteDao()

}