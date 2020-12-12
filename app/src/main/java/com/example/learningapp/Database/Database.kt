package com.example.learningapp.Database

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.learningapp.Dao.HisotryDao
import com.example.learningapp.Entity.HistoryEntity
import com.example.learningapp.notes.Dao.NotesDao
import com.example.learningapp.notes.entities.Note
import java.security.AccessControlContext

@Database(entities = [HistoryEntity::class, Note::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase()
{
    abstract fun gethistoryDao(): HisotryDao
    abstract fun getNotesDao(): NotesDao


    companion object
    {
        @Volatile
        private var instance: com.example.learningapp.Database.Database? = null

        private val NAME: String = "My learning app database"

        fun getDatabase(context: Context): com.example.learningapp.Database.Database
        {

            if (instance != null)
            {
                return instance!!
            }

            synchronized(this)
            {
                val ins = Room
                        .databaseBuilder(context.applicationContext, com.example.learningapp.Database.Database::class.java, NAME)
                        .allowMainThreadQueries()
                        .build()
                instance = ins;
                return ins
            }
        }
    }
}