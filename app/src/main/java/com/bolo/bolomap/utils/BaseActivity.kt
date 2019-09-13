package com.bolo.bolomap.utils

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class BaseActivity: AppCompatActivity() {

    companion object {
        const val PERMISSIONS_WRITE_EXTERNAL_STORAGE = 0
        const val PERMISSIONS_READ_LOCATION = 1

        const val REQUEST_LOCATION_ACTIVATION = 0
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    var isGranted = false

    fun getPermission(manifestPermission: String, permission: Int):Boolean {
        isGranted = false
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, manifestPermission)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission)) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(manifestPermission),
                    permission)

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
                } else {
                }
                return
            } PERMISSIONS_READ_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    isGranted = true
                    onPermissionGranted(PERMISSIONS_READ_LOCATION)
                } else { }
                return
            }

            else -> {
            }
        }
    }

    abstract fun onPermissionGranted(permission: Int)

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