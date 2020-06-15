package com.travel.`in`.db.repository

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import android.os.AsyncTask
import com.travel.`in`.db.dao.PhotoDao
import com.travel.`in`.db.entities.Photo


class PhotoRepository(private val photoDao: PhotoDao) {

    val allPhotos: LiveData<List<Photo>> = photoDao.getAllPhotos()

    @WorkerThread
    fun insert(photo: Photo) {
        photoDao.insert(photo)
    }

    @WorkerThread
    fun update(photo: Photo) {
       photoDao.update(photo)
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




}