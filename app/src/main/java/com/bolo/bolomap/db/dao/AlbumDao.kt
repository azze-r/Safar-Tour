package com.bolo.bolomap.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.bolo.bolomap.db.entities.Album

@Dao
interface AlbumDao {

    @Query("SELECT * FROM album")
    fun getAllAlbums(): LiveData<List<Album>>

    @Query("SELECT * FROM album WHERE id IN (:mediaIds)")
    fun loadAllByIds(mediaIds: IntArray): List<Album>

    @Query("SELECT * FROM album WHERE label LIKE :name LIMIT 1")
    fun findByName(name: String): Album

    @Insert
    fun insertAll(vararg albums: Album)

    @Insert(onConflict = REPLACE)
    fun insert(album: Album)

    @Update
    fun update(album:Album)

    @Delete
    fun delete(album: Album)

    @Query("DELETE FROM album")
    fun deleteAll()
}