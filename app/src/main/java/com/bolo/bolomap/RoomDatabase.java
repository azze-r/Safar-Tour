package com.bolo.bolomap;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.bolo.bolomap.db.dao.MediaDao;
import com.bolo.bolomap.db.entities.Media;

@Database(entities = {Media.class}, version = 1)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

   public abstract MediaDao mediaDao();

   private static volatile RoomDatabase INSTANCE;

   static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "media_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}