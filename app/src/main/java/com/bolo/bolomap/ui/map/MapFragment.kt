package com.bolo.bolomap.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bolo.bolomap.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var homeViewModel: MapViewModel
    private lateinit var mGoogleMap: GoogleMap

    var mMapView: MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(MapViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mMapView = root.findViewById(R.id.mapView)

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

        if (p0 != null) {
            mGoogleMap = p0

            mGoogleMap.setOnMarkerClickListener(this)


        }


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return true
    }



}