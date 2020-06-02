package com.bolo.bolomap.utils

import android.text.TextUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Yvan RAJAONARIVONY on 19/09/2018
 * Copyright (c) 2018 Nouvonivo
 */

object DateUtils {

    private const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.sss'Z'"
    private const val FORM_DATE_FORMAT = "dd/MM/yyyy"
    private const val SEND_DATE_FORMAT = "yyyy-MM-dd"
    var long: Double = 0.0
    var lat: Double = 0.0
    var position: LatLng? = null
    fun dateFromString(strDate: String?): Date? {
        if (TextUtils.isEmpty(strDate)) return null
        return try {
            SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(strDate)
        } catch (e: ParseException) {
            null
        }
    }

    fun dateToFormString(date: Date?): String? {
        if (date == null) return null
        return SimpleDateFormat(FORM_DATE_FORMAT).format(date)
    }

    fun dateToSendFormat(date: Date?): String? {
        if (date == null) return null
        return SimpleDateFormat(SEND_DATE_FORMAT).format(date)
    }

    fun setCameraPosition(pPosition:LatLng){
        position = pPosition
    }

    fun getCameraPosition(): LatLng? {
        return position
    }


}