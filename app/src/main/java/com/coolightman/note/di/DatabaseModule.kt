package com.coolightman.note.di

import android.content.Context
import com.coolightman.note.data.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    fun provideMovieDatabase(context: Context) = AppDatabase.getMovieDatabase(context)

    @Provides
    fun provideNoteDao(db: AppDatabase) = db.noteDao()

    @Provides
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

}