package com.coolightman.note.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coolightman.note.data.database.dao.NoteDao
import com.coolightman.note.data.database.dao.TaskDao
import com.coolightman.note.data.database.dbModel.NoteDb
import com.coolightman.note.data.database.dbModel.TaskDb

@Database(
    version = 6,
    entities = [
        NoteDb::class,
        TaskDb::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao

    companion object {
        private const val DB_NAME = "NoteDatabase.db"

        fun getMovieDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}