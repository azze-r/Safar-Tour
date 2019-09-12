package com.bolo.bolomap.db.repository

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread
import android.os.AsyncTask
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media


class MediaRepository(private val wordDao: MediaDao) {

    val allWords: LiveData<List<Media>> = wordDao.getAllMedias()

    @WorkerThread
    fun insert(media: Media) {
        wordDao.insert(media)
    }

    fun deleteAll() {
        DeleteAllMediasAsyncTask(wordDao).execute()
    }

    private class DeleteAllMediasAsyncTask internal constructor(private val mAsyncTaskDao: MediaDao) :
        AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg voids: Void): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }
    }

    /* ---------------- BORRAR UN SOLO DATO ---------------- */

    fun deleteWord(word: Media) {
        DeleteMediaAsyncTask(wordDao).execute(word)
    }

    private class DeleteMediaAsyncTask internal constructor(private val mAsyncTaskDao: MediaDao) :
        AsyncTask<Media, Void, Void>() {

        override fun doInBackground(vararg params: Media): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }

    /* -------------- ACTUALIZAR UN SOLO DATO ---------------- */

    fun update(word: Media) {
        UpdateMediaAsyncTask(wordDao).execute(word)
    }

    private class UpdateMediaAsyncTask internal constructor(private val mAsyncTaskDao: MediaDao) :
        AsyncTask<Media, Void, Void>() {
        override fun doInBackground(vararg params: Media?): Void? {
            mAsyncTaskDao.update(params[0]!!)
            return null
        }
    }
}