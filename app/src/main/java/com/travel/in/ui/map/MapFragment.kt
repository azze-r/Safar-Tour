package com.travel.`in`.ui.map

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.travel.`in`.R
import com.travel.`in`.db.entities.Photo
import com.travel.`in`.db.viewmodel.MainActivity
import com.travel.`in`.utils.BaseActivity.Companion.PERMISSIONS_READ_LOCATION
import com.travel.`in`.utils.BaseFragment
import com.travel.`in`.utils.MapUtils
import com.travel.`in`.utils.ImageUtils
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var homeViewModel: MapViewModel
    var myMarker: Marker? = null
    var isFABOpen:Boolean ? = null
    var s :Int? = null
    var imageSave: ImageView? = null
    var imgAvatar: ImageView? = null
    var textTitle: TextView? = null
    var cardAlbum: CardView? = null
    var uri: Uri? = null

    var mMapView: MapView? = null
    var myconstraint: CardView? = null
    var currentAlbum:Photo? = null
    lateinit var imgLocation: ImageView
    lateinit var imgDelete: ImageView
    var icon: BitmapDescriptor? = null

    lateinit var photos :ArrayList<Photo>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).statusBarTransparent()
        homeViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        isFABOpen = false
        imageSave = root.findViewById(R.id.imageSave)
        imgAvatar =  root.findViewById(R.id.imgAvatar)
        cardAlbum = root.findViewById(R.id.cardAlbum)
        imgLocation = root.findViewById(R.id.imgLocation)
        imgDelete = root.findViewById(R.id.imgDelete)
        myconstraint = root.findViewById<View>(R.id.constraint) as CardView
        mMapView = root.findViewById(R.id.mapView)
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync(this)

        return root
    }

    override fun onMapReady(p0: GoogleMap?) {

        val act = activity as MainActivity

        if (p0 != null) {

            try {
                p0.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.retro));
            } catch (e:Exception){
                Log.i("tryhard","fk")
            }

            val photoDao = (activity as MainActivity).getDao()

            photoDao!!.getAllPhotos().observe(this,

                Observer {

                    photos = it as ArrayList<Photo>
                    act.mGoogleMap.clear()
                    for (p in photos) {
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.new_location)
                        myMarker = act.mGoogleMap.addMarker(
                            MarkerOptions()
                                .icon(icon)
                                .title(p.label)
                                .position(LatLng(p.lat!!, p.long!!))

                        )


                        myMarker!!.tag = p.id

                    }

                })

            act.mGoogleMap = p0

            if (MapUtils.getCameraPosition() != null) {
                val coordinate = LatLng(
                    MapUtils.getCameraPosition()!!.latitude,
                    MapUtils.getCameraPosition()!!.longitude
                )
                val yourLocation: CameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, MapUtils.getCameraZoo())
                act.mGoogleMap.moveCamera(yourLocation)
            }

            act.mGoogleMap.setOnMarkerClickListener(this)

            act.mGoogleMap.setOnMapClickListener {
                myconstraint?.visibility = View.GONE
                cardAlbum?.visibility = View.GONE
            }

            act.mGoogleMap.setOnMapLongClickListener {


                uri = null
                MapUtils.long = it.longitude
                MapUtils.lat = it.latitude
                cardAlbum?.visibility = View.VISIBLE
                imgAvatar?.setImageResource(R.drawable.baseline_add_photo_alternate_black_48)
                textTitle?.text = ""


            }

            imgLocation.setOnClickListener {
                act.moove = true
                act.getPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    PERMISSIONS_READ_LOCATION
                )
            }

            imageSave!!.setOnClickListener {
                val photo = Photo(
                    long = MapUtils.long,
                    lat = MapUtils.lat,
                    photo = uri.toString(),
                    date = null,
                    description = null,
                    label = "",
                    id = 0,photos= null
                )
                (activity as MainActivity).insertPhoto(photo)
                cardAlbum!!.visibility = View.GONE
                hideKeyboard(requireActivity())
            }


            act.mGoogleMap.setOnCameraMoveListener {
                MapUtils.setCameraPosition(act.mGoogleMap.cameraPosition.target)
                MapUtils.setCameraZoom(act.mGoogleMap.cameraPosition.zoom)
            }

        }


    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {

        myconstraint?.visibility = View.VISIBLE
        val photoDao = (activity as MainActivity).getDao()

        photoDao!!.findById(p0?.tag as Int).observe(this,
            Observer {
                if (it != null) {
                    currentAlbum = it
                    MapUtils.lat = it.lat!!
                    MapUtils.long = it.long!!

                    context?.let { it1 ->

                        if (currentAlbum!!.photos != null) {
                            ImageUtils.loadImageUriResize(
                                ImageUtils.convertStringToArray(currentAlbum!!.photos.toString())[0],
                                R.drawable.ic_dashboard_black_24dp,
                                imgAvatar!!,
                                it1
                            )
                        }
                        else{
                            ImageUtils.loadImageUriResize(
                                it.photo,
                                R.drawable.ic_dashboard_black_24dp,
                                imgAvatar!!,
                                it1
                            )
                        }
                    }

                    if (it.label.isNullOrEmpty())
                        textTitle?.text = "No Title"
                    else
                        textTitle?.text = it.label

                    imgAvatar?.setOnClickListener {
                        val bundle = bundleOf("albumId" to p0.tag)
                        view?.findNavController()
                            ?.navigate(R.id.action_navigation_home_to_navigation_diapo, bundle)
                    }
                }

            })

        imgDelete.setOnClickListener {
            currentAlbum?.let {
                // setup the alert builder
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Do you really want to delete this album?")
                    .setPositiveButton("yes"
                    )
                    { _, _ ->
                        (activity as MainActivity).deletePhoto(it)
                        myconstraint?.visibility = View.GONE
                    }
                    .setNegativeButton("no"
                    ) { dialog, id ->
                        dialog.dismiss()
                    }

                // Create the AlertDialog object and return it
                builder.create()
                builder.show()
            }
        }

        return true
    }


}