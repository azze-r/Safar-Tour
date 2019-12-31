package com.bolo.bolomap.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.bolo.bolomap.db.entities.Photo

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): LiveData<List<Photo>>

    @Query("SELECT * FROM photo WHERE id IN (:mediaIds)")
    fun loadAllByIds(mediaIds: IntArray): List<Photo>

    @Query("SELECT * FROM photo WHERE label LIKE :name LIMIT 1")
    fun findByName(name: String): Photo

    @Query("SELECT * FROM photo WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): LiveData<Photo>

    @Insert
    fun insertAll(vararg photos: Photo)

    @Insert(onConflict = REPLACE)
    fun insert(photo: Photo)

    @Update(onConflict = REPLACE)
    fun update(photo:Photo)

    @Delete
    fun delete(photo: Photo)

    @Query("DELETE FROM photo")
    fun deleteAll()
}