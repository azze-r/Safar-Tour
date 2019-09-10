package com.bolo.bolomap.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class BaseActivity: AppCompatActivity() {

    val PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0
    val PERMISSIONS_READ_LOCATION = 1
    var isGranted = false

    fun getPermission(manifestPermission: String, permission: Int):Boolean {
        isGranted = false
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, manifestPermission)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(manifestPermission),
                    permission)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        else {
            isGranted = true
            onPermissionGranted(permission)
        }

        return isGranted
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    isGranted = true
                    onPermissionGranted(PERMISSIONS_WRITE_EXTERNAL_STORAGE)
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            } PERMISSIONS_READ_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    isGranted = true
                } else { }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    abstract fun onPermissionGranted(permission: Int)

}