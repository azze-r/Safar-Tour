package com.bolo.bolomap;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.bolo.bolomap.db.dao.AlbumDao;
import com.bolo.bolomap.db.dao.PhotoDao;
import com.bolo.bolomap.db.entities.Album;
import com.bolo.bolomap.db.entities.Photo;

@Database(entities = {Album.class, Photo.class}, version = 2)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract AlbumDao mediaDao();
    public abstract PhotoDao photoDao();

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