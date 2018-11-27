package com.jtwaller.attomdemo.network

import com.google.gson.annotations.SerializedName

data class AttomPropertyResponse(
        @SerializedName("property") val attomPropertyList: List<AttomProperty>)

data class AttomProperty(
        val address: AttomPropertyAddress
)

data class AttomPropertyAddress(
        val country: String,
        val line1: String, // address line 1
        val line2: String, // address line 2
        val locality: String,
        val postal1: String
)