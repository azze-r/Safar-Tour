package com.bolo.bolomap

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bolo.bolomap.db.dao.MediaDao
import com.bolo.bolomap.db.entities.Media
import com.bolo.bolomap.utils.BaseActivity
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {

    var mediaDao:MediaDao? = null
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)

        val media = Media(0,null,null,null,null,null,null)

        val database = RoomDatabase.getDatabase(this)

        mediaDao = database.mediaDao()

        val array = ArrayList<Media>()
        array.add(media)
//        mediaDao!!.insertAll(media)
//        insert(media)
    }

    override fun onPermissionGranted(permission: Int) {
        when (permission) {
            PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                dispatchTakePictureIntent()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val tempUri = getImageUri(this, imageBitmap)
//            Toast.makeText(this,tempUri.toString(),Toast.LENGTH_LONG).show()
//            val media = Media(0, Calendar.getInstance().time.toString(),"no name",)
            Toast.makeText(this, tempUri?.let { getRealPathFromURI(it) },Toast.LENGTH_LONG).show()

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
