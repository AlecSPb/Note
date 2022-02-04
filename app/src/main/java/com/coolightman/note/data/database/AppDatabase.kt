package com.coolightman.note.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.coolightman.note.data.database.dao.NoteDao
import com.coolightman.note.data.database.dbModel.NoteDb

@Database(
    version = 4,
    entities = [
        NoteDb::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

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