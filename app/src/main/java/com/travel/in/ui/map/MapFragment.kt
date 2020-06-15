package com.travel.`in`.ui.map

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText


class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var homeViewModel: MapViewModel
    var textInputEditText: TextInputEditText? = null
    var myMarker: Marker? = null
    var isFABOpen:Boolean ? = null
    var imgList: FloatingActionButton? = null
    var imgStyle: FloatingActionButton? = null
    var s :Int? = null
    var imageSave: ImageView? = null
    var imageMenu: ImageView? = null
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

        homeViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        isFABOpen = false
        imageSave = root.findViewById(R.id.imageSave)
        imageMenu = root.findViewById(R.id.imgMenu)
        imgAvatar =  root.findViewById(R.id.imgAvatar)
        cardAlbum = root.findViewById(R.id.cardAlbum)
        imgList = root.findViewById(R.id.imgList)
        imgLocation = root.findViewById(R.id.imgLocation)
        textTitle = root.findViewById(R.id.textTitle)
        imgDelete = root.findViewById(R.id.imgDelete)
        myconstraint = root.findViewById<View>(R.id.constraint) as CardView
        mMapView = root.findViewById(R.id.mapView)
        textInputEditText = root.findViewById(R.id.textInputEditText)
        imgStyle = root.findViewById(R.id.imgStyle)
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync(this)



        imageMenu!!.setOnClickListener {
            if (isFABOpen!!)
                closeFABMenu()
            else
                showFABMenu()

        }
        return root
    }

    override fun onMapReady(p0: GoogleMap?) {

        val act = activity as MainActivity

        if (p0 != null) {

            when (getMapStyle()) {
                0 -> try {
                    p0.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.aubergine))

                } catch (e:Exception){
                    Log.i("tryhard","fk")
                }
                1 -> try {
                    p0.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.dark));
                } catch (e:Exception){
                    Log.i("tryhard","fk")
                }
                2 -> try {
                    p0.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.night));
                } catch (e:Exception){
                    Log.i("tryhard","fk")
                }
                3 -> try {
                    p0.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.retro));
                } catch (e:Exception){
                    Log.i("tryhard","fk")
                }
                4 -> try {
                    p0.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.silver));
                } catch (e:Exception){
                    Log.i("tryhard","fk")
                }
                5 -> try {
                    p0.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.standard));
                    s = 5
                } catch (e:Exception){
                    Log.i("tryhard","fk")
                }
            }


            imgStyle?.setOnClickListener {
                when ((0..5).random()) {
                    0 -> try {
                        p0.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.aubergine))
                        s = 0

                    } catch (e:Exception){
                        Log.i("tryhard","fk")
                    }
                    1 -> try {
                        p0.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.dark));
                        s = 1
                    } catch (e:Exception){
                        Log.i("tryhard","fk")
                    }
                    2 -> try {
                        p0.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.night));
                        s = 2
                    } catch (e:Exception){
                        Log.i("tryhard","fk")
                    }
                    3 -> try {
                        p0.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.retro));
                        s = 3
                    } catch (e:Exception){
                        Log.i("tryhard","fk")
                    }
                    4 -> try {
                        p0.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.silver));
                        s = 4
                    } catch (e:Exception){
                        Log.i("tryhard","fk")
                    }
                    5 -> try {
                        p0.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                context, R.raw.standard));
                        s = 5
                    } catch (e:Exception){
                        Log.i("tryhard","fk")
                    }

                }

            }

            val photoDao = (activity as MainActivity).getDao()

            photoDao!!.getAllPhotos().observe(this,

                Observer {

                    photos = it as ArrayList<Photo>
                    act.mGoogleMap.clear()
                    for (p in photos) {
                        icon = BitmapDescriptorFactory.fromResource(R.mipmap.bleu_location)
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
                    label = textInputEditText?.text.toString(),
                    id = 0,photos= null
                )
                (activity as MainActivity).insertPhoto(photo)
                cardAlbum!!.visibility = View.GONE
                textInputEditText?.text = null

            }

            imgList!!.setOnClickListener {
                it.findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
            }

            act.mGoogleMap.setOnCameraMoveListener {
                MapUtils.setCameraPosition(act.mGoogleMap.cameraPosition.target)
                MapUtils.setCameraZoom(act.mGoogleMap.cameraPosition.zoom)
            }

        }


    }


    override fun onMarkerClick(p0: Marker?): Boolean {

        myconstraint?.visibility = View.VISIBLE
        closeFABMenu()
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
                val builder = AlertDialog.Builder(context!!)
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


    fun getMapStyle(): Int {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        return sharedPref?.getInt(getString(R.string.flag_time), 0)!!

    }

    fun setMapStyle(s:Int){
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        val editor = sharedPref?.edit()
        editor?.putInt(getString(R.string.flag_time), s)
        editor?.apply()
    }

    override fun onPause() {
        super.onPause()
        s?.let { setMapStyle(it) }
    }

    private fun showFABMenu() {

        imageMenu?.setImageDrawable(resources.getDrawable(R.drawable.baseline_remove_black_48))
        myconstraint?.visibility = View.GONE

        isFABOpen = true
        imgList?.animate()?.translationX(-resources.getDimension(R.dimen.standard_75))
        imgStyle?.animate()?.translationY(-resources.getDimension(R.dimen.standard_75))
        imgLocation.animate()?.translationY(-resources.getDimension(R.dimen.standard_75))
        imgLocation.animate()?.translationX(-resources.getDimension(R.dimen.standard_75))

    }

    private fun closeFABMenu() {
        imageMenu?.setImageDrawable(resources.getDrawable(R.drawable.baseline_add_black_48))
        isFABOpen = false
        imgList?.animate()?.translationX(0F)
        imgStyle?.animate()?.translationY(0F)
        imgLocation.animate()?.translationY(0F)
        imgLocation.animate()?.translationX(0F)

    }
}