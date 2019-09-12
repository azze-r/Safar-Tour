package com.bolo.bolomap.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bolo.bolomap.db.entities.Media

@Dao
interface MediaDao {

    @Query("SELECT * FROM media")
    fun getAllMedias(): LiveData<List<Media>>

    @Query("SELECT * FROM media WHERE id IN (:mediaIds)")
    fun loadAllByIds(mediaIds: IntArray): List<Media>

    @Query("SELECT * FROM media WHERE label LIKE :name LIMIT 1")
    fun findByName(name: String): Media

    @Insert
    fun insertAll(vararg medias: Media)

    @Delete
    fun delete(media: Media)
}