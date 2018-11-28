package com.jtwaller.attomdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jtwaller.attomdemo.network.AttomPropertyResponse
import com.jtwaller.attomdemo.network.RestApi
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var restApi: RestApi

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AttomApp.networkComponent.inject(this)

        setContentView(R.layout.activity_main)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        callApi()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val unionSquare = LatLng(37.8, -122.4)
        mMap.addMarker(MarkerOptions().position(unionSquare).title("Union Square"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(unionSquare))
    }

    fun callApi() {
        // "https://search.onboard-apis.com/propertyapi/v1.0.0/avm/detail?address1=11650+ANTWERP+AVE&address2=LOS+ANGELES+CA"
        val resource = "avm"
        val pckg = "detail"
        val params = HashMap<String, String>()
        params.put("address1", "11650 ANTWERP AVE")
        params.put("address2", "LOS ANGELES CA")

        restApi.getProperties(resource, pckg, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object: Observer<AttomPropertyResponse> {
                    override fun onNext(t: AttomPropertyResponse) {
                        Log.d(TAG, ": onNext")
                        Log.d(TAG, ": List size: " + t.attomPropertyList.size);
                        for (property in t.attomPropertyList) {
                            Log.d(TAG, ": " + property.address.line1)
                            Log.d(TAG, ": " + property.address.line2)
                        }
                    }

                    override fun onComplete() {
                        Log.d(TAG, ": onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.d(TAG, ": onSubscribe")
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, ": onError " + e.localizedMessage)
                    }
                })

    }

}
