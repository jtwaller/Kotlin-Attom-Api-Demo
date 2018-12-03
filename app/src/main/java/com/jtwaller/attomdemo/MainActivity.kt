package com.jtwaller.attomdemo

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jtwaller.attomdemo.network.AttomPropertyResponse
import com.jtwaller.attomdemo.network.RestApi
import com.jtwaller.attomdemo.ui.AboutDialogFragment
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setSearchRadius -> {
                displaySearchRadiusDialog()
                true
            }
            R.id.setAvmBounds -> {
                displaySetAvmBoundsDialog()
                true
            }
            R.id.about -> {
                displayAboutDialog()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun displaySearchRadiusDialog() {
        Log.d(TAG, ": Show search radius dialog")
    }

    fun displaySetAvmBoundsDialog() {
        Log.d(TAG, ": Show avm bounds dialog")
    }

    fun displayAboutDialog() {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        AboutDialogFragment().show(ft, "dialog")
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
        val unionSquare = LatLng(37.788179, -122.406982)
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(unionSquare).title("Union Square"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(unionSquare, 13f))

        callApi(unionSquare, 1f)
    }

    fun callApi(latLng: LatLng, radius: Float) {
        // eg https://search.onboard-apis.com/propertyapi/v1.0.0/property/snapshot?latitude=39.296864&longitude=-75.613574&radius=20
        val resource = "avm"
        val pckg = "snapshot"
        val params = HashMap<String, String>()
        params.put("latitude", latLng.latitude.toString())
        params.put("longitude", latLng.longitude.toString())
        params.put("pageSize", "100")
        params.put("orderBy", "avmValue desc")
        params.put("radius", radius.toString())

        restApi.getProperties(resource, pckg, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object: Observer<AttomPropertyResponse> {

                    val progressDialog = ProgressDialog(this@MainActivity)

                    init {
                        progressDialog.isIndeterminate = true
                        progressDialog.setMessage(getString(R.string.fetching_data))
                    }

                    override fun onNext(t: AttomPropertyResponse) {
                        Log.d(TAG, ": onNext")
                        Log.d(TAG, ": List size: " + t.attomPropertyList.size);
                        for (property in t.attomPropertyList) {
                            val pos = LatLng(property.location.latitude.toDouble(), property.location.longitude.toDouble())
                            mMap.addMarker(MarkerOptions().position(pos).title("Union Square"))

//                            Log.d(TAG, ": " + property.address.line1)
//                            Log.d(TAG, ": " + property.address.line2)
//                            Log.d(TAG, ": ");
                        }
                    }

                    override fun onComplete() {
                        Log.d(TAG, ": onComplete")
                        progressDialog.dismiss()
                    }

                    override fun onSubscribe(d: Disposable) {
                        Log.d(TAG, ": onSubscribe")
                        progressDialog.show()
                    }

                    override fun onError(e: Throwable) {
                        Log.d(TAG, ": onError " + e.localizedMessage)
                        progressDialog.dismiss()
                    }
                })
    }
}
