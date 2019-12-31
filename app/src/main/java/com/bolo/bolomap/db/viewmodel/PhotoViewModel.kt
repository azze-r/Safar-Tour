package com.bolo.bolomap.db.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.db.repository.PhotoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PhotoViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: PhotoRepository
    val allPhotos: LiveData<List<Photo>>

    init {
        val photosDao = RoomDatabase.getDatabase(application).photoDao()
        repository = PhotoRepository(photosDao)
        allPhotos = repository.allWords
    }

    fun insert(photo:Photo) = scope.launch(Dispatchers.IO) {
        repository.insert(photo)
    }

    fun update(photo:Photo) = scope.launch(Dispatchers.IO) {
        repository.update(photo)
    }


    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    fun deletePhoto(photo: Photo) {
        repository.deletePhoto(photo)
    }

}