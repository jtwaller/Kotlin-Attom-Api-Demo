package com.jtwaller.attomdemo.network

import io.reactivex.Observable
import javax.inject.Inject

class RestApi @Inject constructor(private val attomApi: AttomApi) {

    fun getProperties(resource: String, pckg: String, queryMap: Map<String, String>): Observable<AttomPropertyResponse> {
        return attomApi.getProperties(resource, pckg, queryMap)
    }

}