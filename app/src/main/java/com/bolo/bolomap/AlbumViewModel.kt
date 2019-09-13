package com.bolo.bolomap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bolo.bolomap.db.entities.Album
import com.bolo.bolomap.db.repository.AlbumRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: AlbumRepository
    val allAlbums: LiveData<List<Album>>

    init {
        val albumsDao = RoomDatabase.getDatabase(application).mediaDao()
        repository = AlbumRepository(albumsDao)
        allAlbums = repository.allAlbums
    }

    fun insert(album:Album) = scope.launch(Dispatchers.IO) {
        repository.insert(album)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun deleteAll() {
        repository.deleteAll()
    }

    fun deleteAlbum(album: Album) {
        repository.deletePhoto(album)
    }

    fun updateAlbum(album: Album) {
        repository.update(album)
    }

}