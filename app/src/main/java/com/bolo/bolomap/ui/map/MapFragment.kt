package com.bolo.bolomap.ui.map

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bolo.bolomap.MainActivity
import com.bolo.bolomap.R
import com.bolo.bolomap.utils.BaseActivity
import com.bolo.bolomap.utils.BaseActivity.Companion.PERMISSIONS_READ_LOCATION
import com.bolo.bolomap.utils.BaseFragment
import com.bolo.bolomap.utils.ImageUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*

class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var homeViewModel: MapViewModel

    var myMarker: Marker? = null
    var mMapView: MapView? = null
    var myconstraint: CardView? = null

    lateinit var fab1: FloatingActionButton
    lateinit var fab2: FloatingActionButton
    lateinit var fab3: FloatingActionButton
    var isFABOpen = false
    lateinit var imageView:ImageView
    var mDefaultLocation : LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {

        homeViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val fab: FloatingActionButton = root.findViewById(R.id.fab)
        fab1 = root.findViewById(R.id.fab1)
        fab2 = root.findViewById(R.id.fab2)
        fab3 = root.findViewById(R.id.fab3)
        imageView = root.findViewById(R.id.imageView)
        myconstraint = root.findViewById<View>(R.id.constraint) as CardView
        mMapView = root.findViewById(R.id.mapView)
        mDefaultLocation = LatLng(12.0,70.0)
        fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        fab2.setOnClickListener {
            val act = activity as MainActivity
            act.getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, BaseActivity.PERMISSIONS_WRITE_EXTERNAL_STORAGE)
        }

        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()



        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        mMapView!!.getMapAsync(this)

//        homeViewModel.text.observe(this, Observer {
//            textView.text = it
//        })

        return root
    }

    override fun onMapReady(p0: GoogleMap?) {
        val act =  activity as MainActivity

        if (p0 != null) {
            act.mGoogleMap = p0
            act.mGoogleMap.setOnMarkerClickListener(this)

            val drawable = R.drawable.ic_home_black_24dp
            val icon = BitmapDescriptorFactory.fromResource(drawable)

            myMarker = act.mGoogleMap.addMarker(
                MarkerOptions()
                    .icon(context?.let { bitmapDescriptorFromVector(it, R.drawable.ic_notifications_black_24dp) })
                    .position(
                        LatLng(43.6329,6.9991)
                    ))

            myMarker = act.mGoogleMap.addMarker(
                MarkerOptions()
                    .icon(context?.let { bitmapDescriptorFromVector(it, R.drawable.ic_notifications_black_24dp) })
                    .position(
                        LatLng(48.210033,16.363449)
                    ))


            act.mGoogleMap.setOnMapClickListener {
                myconstraint?.visibility = View.GONE
            }



            imageView.setOnClickListener {
                act.moove = true
                act.getPermission(Manifest.permission.ACCESS_FINE_LOCATION,PERMISSIONS_READ_LOCATION)
            }
        }



    }

    override fun onMarkerClick(p0: Marker?): Boolean {

        myconstraint?.visibility = View.VISIBLE

        context?.let {
            ImageUtils.loadImageResize("https://static.thenounproject.com/png/166349-200.png", R.drawable.ic_dashboard_black_24dp, cardAvatar,
                it
            )
        }

        textTitle.text = "name"
        textDesc.text = "address"
        textPhone.text = "phone"
        return true
    }

    private fun showFABMenu() {
        isFABOpen = true
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_105))
        fab3.animate().translationY(-resources.getDimension(R.dimen.standard_155))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fab1.animate().translationY(0F)
        fab2.animate().translationY(0F)
        fab3.animate().translationY(0F)
    }

    private fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor {
        val background = ContextCompat.getDrawable(context, R.drawable.ic_notifications_black_24dp)
        background!!.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        vectorDrawable!!.setBounds(
            40,
            20,
            vectorDrawable.intrinsicWidth + 40,
            vectorDrawable.intrinsicHeight + 20
        )
        val bitmap = Bitmap.createBitmap(
            background.intrinsicWidth,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    companion object {
        const val EXTRA_REPLY = "com.jeluchu.roombbdd.REPLY"
    }
}