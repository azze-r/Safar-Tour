package com.bolo.bolomap

import android.os.AsyncTask
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media

class MainActivity : AppCompatActivity() {

    var mediaDao:MediaDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        val media = Media(0,null,null,null,null,null,null)

        val database = WordRoomDatabase.getDatabase(this)

        mediaDao = database.mediaDao()

        val array = ArrayList<Media>()
        array.add(media)



//        mediaDao!!.insertAll(media)
        insert(media)


    }

    fun insert(media: Media) {
        insertAsyncTask(mediaDao!!).execute(media)
    }

    private class insertAsyncTask internal constructor(private val mAsyncTaskDao: MediaDao) :
        AsyncTask<Media, Void, Void>() {

        override fun doInBackground(vararg params: Media): Void? {
            mAsyncTaskDao.insertAll(params[0])
            return null
        }
    }

}
