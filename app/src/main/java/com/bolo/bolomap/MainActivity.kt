package com.bolo.bolomap

import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.utils.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MainActivity : BaseActivity() {

    var mediaDao:MediaDao? = null
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        val media = Media(1,null,null,null,null,null,null)

        val database = RoomDatabase.getDatabase(this)

        mediaDao = database.mediaDao()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val array = ArrayList<Media>()
        array.add(media)
//        mediaDao!!.insertAll(media)
//        insert(media)
    }

    override fun onPermissionGranted(permission: Int) {
        when (permission) {
            Companion.PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                dispatchTakePictureIntent()
            }
            PERMISSIONS_READ_LOCATION -> {
                mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    location?.longitude?.let { it1 -> mooveToLatLng(location.latitude, it1) }
                }

            }
        }
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
    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            this.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    fun mooveToLatLng(lat:Double, long:Double){
        mGoogleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lat, long),
                12.0f
            ))
    }

}
