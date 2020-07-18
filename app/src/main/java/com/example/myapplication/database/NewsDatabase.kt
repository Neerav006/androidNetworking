package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.room.NewsRoom


    @Database(entities = arrayOf(NewsRoom::class), version = 1, exportSchema = false)
    public abstract class NewsDatabase : RoomDatabase() {

        abstract fun newsDao(): NewsDao

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: NewsDatabase? = null

            fun getDatabase(context: Context): NewsDatabase {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        "news_database"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
