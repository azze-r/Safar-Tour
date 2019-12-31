package com.bolo.bolomap.ui.map

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
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
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_home.*


class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var homeViewModel: MapViewModel


    var textInputEditText: TextInputEditText? = null

    var myMarker: Marker? = null
    var imgList: ImageView? = null
    var imageSave: ImageView? = null
    var imageAdd: ImageView? = null
    var cardAlbum: CardView? = null
    var uri: Uri? = null
    var bitmap: Bitmap? = null
    var long: Double = 0.0
    var lat: Double = 0.0
    var mMapView: MapView? = null
    var myconstraint: CardView? = null
    lateinit var imageView: ImageView
    var mDefaultLocation: LatLng? = null
    lateinit var photos :ArrayList<Photo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        imageSave = root.findViewById(R.id.imageSave)
        imageAdd = root.findViewById(R.id.imageAdd)
        cardAlbum = root.findViewById(R.id.cardAlbum)
        imgList = root.findViewById(R.id.imgList)
        imageView = root.findViewById(R.id.imgLocation)
        myconstraint = root.findViewById<View>(R.id.constraint) as CardView
        mMapView = root.findViewById(R.id.mapView)
        textInputEditText = root.findViewById(R.id.textInputEditText)
        mDefaultLocation = LatLng(12.0, 70.0)

        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync(this)

        imgList!!.setOnClickListener {
            it.findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
        }

        return root
    }

    override fun onMapReady(p0: GoogleMap?) {

        val act = activity as MainActivity

        if (p0 != null) {

            act.mGoogleMap = p0
            act.mGoogleMap.setOnMarkerClickListener(this)

            act.mGoogleMap.setOnMapClickListener {
                myconstraint?.visibility = View.GONE
                cardAlbum?.visibility = View.GONE
            }

            act.mGoogleMap.setOnMapLongClickListener {
                long = it.longitude
                lat = it.latitude
                cardAlbum?.visibility = View.VISIBLE
            }

            imageView.setOnClickListener {
                act.moove = true
                act.getPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    PERMISSIONS_READ_LOCATION
                )
            }

            imageSave!!.setOnClickListener {
                val photo = Photo(
                    long = long,
                    lat = lat,
                    photo = uri.toString(),
                    date = null,
                    description = null,
                    label = textInputEditText?.text.toString(),
                    id = 0,photos= null
                )

                (activity as MainActivity).insertPhoto(photo)

                cardAlbum!!.visibility = View.GONE

                myMarker = act.mGoogleMap.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .position(
                            LatLng(lat,long)
                        ))

                imageAdd?.setImageResource(R.drawable.baseline_add_photo_alternate_black_48);
                textInputEditText?.text = null
            }


            imageAdd!!.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_OPEN_DOCUMENT
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 1)
            }


            val photoDao = (activity as MainActivity).getDao()

            photoDao!!.getAllPhotos().observe(this,
                Observer {
                    photos = it as ArrayList<Photo>
                    for (p in photos) {
                        var icon = getBitmap(context?.contentResolver, p.photo?.toUri())
                        icon = Bitmap.createScaledBitmap(icon!!, 100, 100, false)
                        myMarker = act.mGoogleMap.addMarker(
                            MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                .position(
                                    LatLng(p.lat!!, p.long!!)
                                )
                        )
                    }
                })

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
                imageAdd!!.setImageURI(uri)
                bitmap = getBitmap(context?.contentResolver, uri)
                val height = 100
                val width = 100
                bitmap = Bitmap.createScaledBitmap(bitmap!!, width, height, false)
            }
        }
    }


}