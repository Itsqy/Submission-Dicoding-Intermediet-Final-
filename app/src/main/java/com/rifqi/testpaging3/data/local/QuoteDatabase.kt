package com.rifqi.testpaging3.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rifqi.testpaging3.data.remote.ListStoryData

@Database(
    entities = [ListStoryData::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
    abstract fun remoteDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: QuoteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): QuoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QuoteDatabase::class.java, "quote_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}