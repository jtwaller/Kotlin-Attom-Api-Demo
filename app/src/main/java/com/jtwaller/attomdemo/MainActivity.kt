package com.jtwaller.attomdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jtwaller.attomdemo.network.RestApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var restApi: RestApi

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AttomApp.networkComponent.inject(this)

        setContentView(R.layout.activity_main)

        // "https://search.onboard-apis.com/propertyapi/v1.0.0/avm/detail?address1=11650+ANTWERP+AVE&address2=LOS+ANGELES+CA"
        val resource = "avm"
        val pckg = "detail"
        val params = HashMap<String, String>()
        params.put("address1", "11650 ANTWERP AVE")
        params.put("address2", "LOS ANGELES CA")

        var result = restApi.getProperties(resource, pckg, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}
