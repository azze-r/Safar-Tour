package com.travel.`in`.utils

import com.google.android.gms.maps.model.LatLng


/**
 * Created by Yvan RAJAONARIVONY on 19/09/2018
 * Copyright (c) 2018 Nouvonivo
 */

object MapUtils {

    var long: Double = 0.0
    var lat: Double = 0.0
    var position: LatLng? = null
    var zoom: Float = 0F

    fun setCameraPosition(pPosition:LatLng){
        position = pPosition
    }

    fun getCameraPosition(): LatLng? {
        return position
    }

    fun setCameraZoom(pZoom: Float) {
        zoom = pZoom
    }

    fun getCameraZoo(): Float {
        return zoom
    }


}