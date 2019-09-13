package com.bolo.bolomap.db.repository

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import android.os.AsyncTask
import com.bolo.bolomap.db.dao.AlbumDao
import com.bolo.bolomap.db.dao.PhotoDao
import com.bolo.bolomap.db.entities.Album
import com.bolo.bolomap.db.entities.Photo


class PhotoRepository(private val photoDao: PhotoDao) {

    val allWords: LiveData<List<Photo>> = photoDao.getAllPhotos()

    @WorkerThread
    fun insert(photo: Photo) {
        photoDao.insert(photo)
    }

    fun deleteAll() {
        DeleteAllPhotosAsyncTask(photoDao).execute()
    }

    private class DeleteAllPhotosAsyncTask internal constructor(private val mAsyncTaskDao: PhotoDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }

    /* ---------------- BORRAR UN SOLO DATO ---------------- */

    fun deletePhoto(photo: Photo) {
        DeletePhotoAsyncTask(photoDao).execute(photo)
    }

    private class DeletePhotoAsyncTask internal constructor(private val mAsyncTaskDao: PhotoDao) :
        AsyncTask<Photo, Void, Void>() {

        override fun doInBackground(vararg params: Photo): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }

    /* -------------- ACTUALIZAR UN SOLO DATO ---------------- */

    fun update(photo: Photo) {
        UpdatePhotoAsyncTask(photoDao).execute(photo)
    }

    private class UpdatePhotoAsyncTask internal constructor(private val mAsyncTaskDao: PhotoDao) :
        AsyncTask<Photo, Void, Void>() {
        override fun doInBackground(vararg params: Photo?): Void? {
            mAsyncTaskDao.update(params[0]!!)
            return null
        }
    }
}