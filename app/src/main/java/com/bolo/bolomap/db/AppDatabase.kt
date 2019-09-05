package com.bolo.bolomap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media

@Database(entities = arrayOf(Media::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao


    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            if (INSTANCE == null) {

                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        // Get PhraseRoomDatabase database instance
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "media_database"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}