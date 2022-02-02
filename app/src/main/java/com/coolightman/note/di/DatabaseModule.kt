package com.coolightman.note.di

import android.content.Context
import com.coolightman.note.data.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @ApplicationScope
    @Provides
    fun provideMovieDatabase(context: Context) = AppDatabase.getMovieDatabase(context)

    @ApplicationScope
    @Provides
    fun provideNoteDao(db: AppDatabase) = db.noteDao()

}