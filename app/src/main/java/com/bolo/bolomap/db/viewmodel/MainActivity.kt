package com.bolo.bolomap.db.viewmodel


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bolo.bolomap.R
import com.bolo.bolomap.db.dao.PhotoDao
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.utils.BaseActivity
import com.bolo.bolomap.utils.ImageUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity() {

    var moove = false
    var fast = false
    var long = 0.0
    var lat = 0.0

    var photos = ""
    val newWordActivityRequestCode = 1
    private lateinit var photoViewModel: PhotoViewModel
    var tempUri:Uri? = null
    var photoDao:PhotoDao? = null

    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        val database = RoomDatabase.getDatabase(this)

        photoDao = database.photoDao()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        photoViewModel = ViewModelProviders.of(this).get(PhotoViewModel::class.java)
    }

    override fun onPermissionGranted(permission: Int) {
        when (permission) {

            PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                getPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSIONS_READ_LOCATION)
            }

            PERMISSIONS_READ_LOCATION -> {

                if (moove) {
                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (!(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                            && !(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            alertLocationDisabled()
                        }
                        else location?.longitude?.let { it1 ->
                            mooveToLatLng(location.latitude, it1)
                        }
                        fast = false
                    }
                }

                else{

                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            location: Location? ->
                        location?.longitude?.let {
                                it1 ->
                            val replyIntent = Intent()
                            long = it1
                            lat = location.latitude
                            replyIntent.putExtra("com.jeluchu.roombbdd.REPLY", photos)
                            setResult(Activity.RESULT_OK, replyIntent)
                            dispatchTakePictureIntent()
                        }
                    }
                    moove = false
                }
            }
        }
    }

    fun insertPhoto(photo: Photo) {
        InsertAsyncTask(photoDao!!).execute(photo)
    }

    fun getDao(): PhotoDao? {
        return photoDao
    }

    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: PhotoDao) :
        AsyncTask<Photo, Void, Void>() {

        override fun doInBackground(vararg params: Photo): Void? {
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
        if (mGoogleMap != null) {

            mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(lat, long),
                    12.0f
                )
            )
        }
    }


    fun putPicToLatLng(lat:Double, long:Double){
        mGoogleMap.addMarker(
            MarkerOptions()
                .icon(this.let { ImageUtils.bitmapDescriptorFromVector(it,
                    R.mipmap.ic_launcher_travel_rounded
                ) })
                .position(
                    LatLng(lat,long)
                ))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = intent?.extras?.get("data") as Bitmap
            tempUri = ImageUtils.getImageUri(this, imageBitmap)
            Toast.makeText(this, tempUri?.let { ImageUtils.getRealPathFromURI(it,this) },Toast.LENGTH_LONG).show()
            photos = tempUri?.let { ImageUtils.getRealPathFromURI(it,this) }.toString()
            val photo = Photo(0,null,null,long,lat,photos,null)
            photoViewModel.insert(photo)
            Log.i("tryhard",photo.toString())

            putPicToLatLng(lat,long)
        }

        else if (requestCode == REQUEST_LOCATION_ACTIVATION && resultCode == RESULT_OK) {

            mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->

                if ((getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && (getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    location?.longitude?.let {
                            it1 -> mooveToLatLng(location.latitude, it1)
                    }
                }
            }

        }
        }



}
