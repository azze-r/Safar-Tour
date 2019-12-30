package com.bolo.bolomap.ui.map

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProviders
import com.bolo.bolomap.R
import com.bolo.bolomap.db.entities.Photo
import com.bolo.bolomap.db.viewmodel.MainActivity
import com.bolo.bolomap.utils.BaseActivity.Companion.PERMISSIONS_READ_LOCATION
import com.bolo.bolomap.utils.BaseFragment
import com.bolo.bolomap.utils.ImageUtils
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_home.*


class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var homeViewModel: MapViewModel

    var myMarker: Marker? = null
    var imageSave: ImageView? = null
    var imageAdd: ImageView? = null
    var cardAlbum: CardView? = null
    var uri: Uri? = null
    var path: String? = null

    var long: Double = 0.0
    var lat: Double = 0.0
    var mMapView: MapView? = null
    var myconstraint: CardView? = null

    lateinit var imageView: ImageView
    var mDefaultLocation: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        imageSave = root.findViewById(R.id.imageSave)
        imageAdd = root.findViewById(R.id.imageAdd)
        cardAlbum = root.findViewById(R.id.cardAlbum)

        imageSave!!.setOnClickListener {
            val photo = Photo(
                long = long,
                lat = lat,
                photo = path,
                date = null,
                description = null,
                label = null,
                id = 0
            )
            (activity as MainActivity).insertPhoto(photo)
            cardAlbum!!.visibility = View.GONE
        }


        imageAdd!!.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select picture"), 1)
        }



        imageView = root.findViewById(R.id.imageView)
        myconstraint = root.findViewById<View>(R.id.constraint) as CardView
        mMapView = root.findViewById(R.id.mapView)
        mDefaultLocation = LatLng(12.0, 70.0)


        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync(this)

        return root
    }

    override fun onMapReady(p0: GoogleMap?) {

        val act = activity as MainActivity

        if (p0 != null) {

            act.mGoogleMap = p0
            act.mGoogleMap.setOnMarkerClickListener(this)

//            myMarker = act.mGoogleMap.addMarker(
//                MarkerOptions()
//                    .icon(context?.let { ImageUtils.bitmapDescriptorFromVector(it, R.drawable.map_marker) })
//                    .position(
//                        LatLng(43.6329,6.9991)
//                    ))
//


            act.mGoogleMap.setOnMapClickListener {
                myconstraint?.visibility = View.GONE
                cardAlbum?.visibility = View.GONE
            }

            act.mGoogleMap.setOnMapLongClickListener {
                long = it.longitude
                lat = it.latitude
                cardAlbum?.visibility = View.VISIBLE
                act.mGoogleMap.addMarker(
                    MarkerOptions().position(it).icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_ORANGE
                        )
                    )
                )
            }

            imageView.setOnClickListener {
                act.moove = true
                act.getPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    PERMISSIONS_READ_LOCATION
                )
            }

        }


    }

    override fun onMarkerClick(p0: Marker?): Boolean {

        myconstraint?.visibility = View.VISIBLE

        context?.let {
            ImageUtils.loadImageResize(
                "https://static.thenounproject.com/png/166349-200.png",
                R.drawable.ic_dashboard_black_24dp,
                cardAvatar,
                it
            )
        }

        textTitle.text = "name"
        textDesc.text = "address"
        textPhone.text = "phone"
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                uri = data?.data!!
                path = getRealPathFromURI(uri!!, activity as MainActivity)
                imageAdd!!.setImageURI(uri)

                }
            }
        }


    fun getRealPathFromURI(contentURI: Uri, context: Activity): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.managedQuery(contentURI, projection, null, null, null) ?: return null
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        return if (cursor.moveToFirst()) {
            // cursor.close();
            cursor.getString(column_index)
        } else
            null
        // cursor.close();
    }

}