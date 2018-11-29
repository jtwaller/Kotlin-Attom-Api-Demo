package com.jtwaller.attomdemo.network

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class AttomPropertyResponse(
        @SerializedName("property") val attomPropertyList: List<AttomProperty>)

data class AttomProperty(
        val address: AttomPropertyAddress,
        val location: AttomPropertyLocation) {

    fun position(): LatLng {
        return LatLng(location.latitude.toDouble(), location.longitude.toDouble())
    }

}

data class AttomPropertyAddress(
        val country: String,
        val line1: String, // address line 1
        val line2: String, // address line 2
        val locality: String,
        val postal1: String)

data class AttomPropertyLocation(
        val latitude: String,
        val longitude: String)