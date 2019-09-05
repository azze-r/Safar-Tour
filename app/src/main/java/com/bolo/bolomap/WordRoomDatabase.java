package com.bolo.bolomap;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bolo.bolomap.db.dao.MediaDao;
import com.bolo.bolomap.db.entities.Media;

@Database(entities = {Media.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase {

   public abstract MediaDao mediaDao();

   private static volatile WordRoomDatabase INSTANCE;

   static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "media_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}