package com.jtwaller.attomdemo.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface AttomApi {
    @GET("propertyapi/v1.0.0/{resource}/{package}")
    fun getProperties(@Path("resource") resource: String,
                     @Path("package") pckg: String,
                     @QueryMap queryMap: Map<String, String>)
        : Observable<AttomPropertyResponse>
}