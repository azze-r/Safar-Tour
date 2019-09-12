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


class MainActivity : BaseActivity() {

    var moove = false
    var fast = false
    var photos = ""
    val newWordActivityRequestCode = 1
    private lateinit var mediaViewModel: MediaViewModel
    var tempUri:Uri? = null
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

        mediaViewModel = ViewModelProviders.of(this).get(MediaViewModel::class.java)

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
//                            insert(Media(0, Calendar.getInstance().time.toString(),
//                            "no name",location.latitude,it1,photos,null))

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = intent?.extras?.get("data") as Bitmap
            tempUri = getImageUri(this, imageBitmap)

            Toast.makeText(this, tempUri?.let { getRealPathFromURI(it) },Toast.LENGTH_LONG).show()
            photos = tempUri?.let { getRealPathFromURI(it) }.toString()
            getPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSIONS_READ_LOCATION)
        }  else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mFusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if ((getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && (getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location?.longitude?.let { it1 -> mooveToLatLng(location.latitude, it1) }
                }
            }
        }

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intent?.let { data ->
                val media = Media(0,null,null,null,null,photos,null)
                mediaViewModel.insert(media)
            }
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

    fun alertLocationDisabled() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Localisation désactivée")
        builder.setMessage("Pour utiliser la fonctionnalité vous devez activer la localisation.")
        builder.setPositiveButton("Activer") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(intent, REQUEST_LOCATION_ACTIVATION)
        }
        builder.setNegativeButton("Non, merci"){ dialog, which ->}
        val dialog = builder.create()
        dialog.show()
    }
}
