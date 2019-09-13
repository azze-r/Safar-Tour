package com.bolo.bolomap.db.repository

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import android.os.AsyncTask
import com.bolo.bolomap.db.dao.AlbumDao
import com.bolo.bolomap.db.entities.Album


class AlbumRepository(private val albumDao: AlbumDao) {

    val allAlbums: LiveData<List<Album>> = albumDao.getAllAlbums()

    @WorkerThread
    fun insert(album: Album) {
        albumDao.insert(album)
    }

    fun deleteAll() {
        DeleteAllAlbumsAsyncTask(albumDao).execute()
    }

    private class DeleteAllAlbumsAsyncTask internal constructor(private val mAsyncTaskDao: AlbumDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }

    fun deletePhoto(album: Album) {
        DeleteAlbumAsyncTask(albumDao).execute(album)
    }

    private class DeleteAlbumAsyncTask internal constructor(private val mAsyncTaskDao: AlbumDao) :
        AsyncTask<Album, Void, Void>() {

        override fun doInBackground(vararg params: Album): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }


    fun update(album: Album) {
        UpdateAlbumAsyncTask(albumDao).execute(album)
    }

    private class UpdateAlbumAsyncTask internal constructor(private val mAsyncTaskDao: AlbumDao) :
        AsyncTask<Album, Void, Void>() {
        override fun doInBackground(vararg params: Album?): Void? {
            mAsyncTaskDao.update(params[0]!!)
            return null
        }
    }
}