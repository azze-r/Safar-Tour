package com.bolo.bolomap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bolo.bolomap.RoomDatabase
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.db.repository.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MediaViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: MediaRepository
    val allWords: LiveData<List<Media>>

    init {
        val wordsDao = RoomDatabase.getDatabase(application).mediaDao()
        repository = MediaRepository(wordsDao)
        allWords = repository.allWords
    }

    fun insert(media:Media) = scope.launch(Dispatchers.IO) {
        repository.insert(media)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    // BORRAR TODOS LOS DATOS
    fun deleteAll() {
        repository.deleteAll()
    }

    // BORRAR UN SOLO DATO
    fun deleteWord(media: Media) {
        repository.deleteWord(media)
    }

    fun updateWord(media: Media) {
        repository.update(media)
    }

}