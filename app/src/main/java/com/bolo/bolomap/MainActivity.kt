package com.bolo.bolomap


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bolo.bolomap.db.dao.AlbumDao
import com.bolo.bolomap.db.dao.PhotoDao
import com.bolo.bolomap.db.entities.Album
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.utils.BaseActivity
import com.bolo.bolomap.utils.ImageUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream
import kotlin.random.Random


class MainActivity : BaseActivity() {

    var moove = false
    var fast = false
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

    fun getDao(): AlbumDao? {
        val database = RoomDatabase.getDatabase(this)
        return database.mediaDao()
    }

    override fun onPermissionGranted(permission: Int) {
        when (permission) {

            PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                dispatchTakePictureIntent()
            }

            PERMISSIONS_READ_LOCATION -> {

                if (moove) {
                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        if (!(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                            && !(getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            alertLocationDisabled()
                        } else location?.longitude?.let { it1 -> mooveToLatLng(location.latitude, it1) }
                        fast = false
                    }
                }

                if (fast){

                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            location: Location? ->
                        location?.longitude?.let {
                                it1 ->
                            val replyIntent = Intent()
                            replyIntent.putExtra("com.jeluchu.roombbdd.REPLY", photos)
                            setResult(Activity.RESULT_OK, replyIntent)
                        }
                    }
                    moove = false
                }
            }
        }
    }

    fun insert(photo: Photo) {
        InsertAsyncTask(photoDao!!).execute(photo)
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
        mGoogleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lat, long),
                12.0f
            ))
    }

    fun putPicToLatLng(lat:Double, long:Double){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = intent?.extras?.get("data") as Bitmap
            tempUri = ImageUtils.getImageUri(this, imageBitmap)
            Toast.makeText(this, tempUri?.let { ImageUtils.getRealPathFromURI(it,this) },Toast.LENGTH_LONG).show()
            photos = tempUri?.let { ImageUtils.getRealPathFromURI(it,this) }.toString()
            getPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSIONS_READ_LOCATION)

        }

        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->

                if ((getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && (getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    location?.longitude?.let {
                            it1 -> mooveToLatLng(location.latitude, it1)
                    }
                }
            }

        }

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intent?.let { data ->
                val photo = Photo(0,null,null,null,null,photos,null)
                photoViewModel.insert(photo)
            }
        }
    }




}
