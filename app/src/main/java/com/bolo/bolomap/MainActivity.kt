package com.bolo.bolomap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.utils.BaseActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : BaseActivity() {

    var moove = false
    var fast = false
    var photos = ""

    var mediaDao:MediaDao? = null
    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        val media = Media(0,null,null,null,null,null,null)

        val database = RoomDatabase.getDatabase(this)

        mediaDao = database.mediaDao()
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val array = ArrayList<Media>()
        array.add(media)
//        mediaDao!!.insertAll(media)
//        insert(media)
    }

    fun getDao(): MediaDao? {
        val database = RoomDatabase.getDatabase(this)
        return database.mediaDao()
    }

    override fun onPermissionGranted(permission: Int) {
        when (permission) {

            Companion.PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                dispatchTakePictureIntent()
            }

            PERMISSIONS_READ_LOCATION -> {
                if (moove) {
                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.
                        location?.longitude?.let { it1 -> mooveToLatLng(location.latitude, it1) }
                    }
                    fast = false
                }

                if (fast){

                    mFusedLocationProviderClient.lastLocation.addOnSuccessListener {
                            location: Location? ->
                        location?.longitude?.let {
                                it1 -> insert(Media(0, Calendar.getInstance().time.toString(),
                            "no name",location.latitude,it1,photos,null))
                        }
                    }
                    moove = false
                }
            }
        }
    }

    fun insert(media: Media) {
        InsertAsyncTask(mediaDao!!).execute(media)
    }


    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: MediaDao) :
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val tempUri = getImageUri(this, imageBitmap)
            Toast.makeText(this, tempUri?.let { getRealPathFromURI(it) },Toast.LENGTH_LONG).show()
            photos = tempUri?.let { getRealPathFromURI(it) }.toString()
            getPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSIONS_READ_LOCATION)
        }
    }

    private fun getImageUri(inContext: Context, inImage:Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri:Uri): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver!!.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }

        return path
    }




}
