package com.uniimarket.app

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private var mMap: GoogleMap? = null
    internal lateinit var mLastLocation: Location
    internal lateinit var mLocationResult: LocationRequest
    internal lateinit var mLocationCallback: LocationCallback
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    internal var mFusedLocationClient: FusedLocationProviderClient? = null
    var mapFragment: SupportMapFragment? = null
    var btn_pic_location: Button? = null
    var lat: Double = 0.0
    var longi: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_maps)

        initialVariables()

        btn_pic_location?.setOnClickListener {
            if (Helper.pointLongitude == 0.0 && Helper.pointLatitude == 0.0) {
                Helper.pointLongitude = Helper.longitude
                Helper.pointLatitude = Helper.latitude
            } else{

            }
            finish()
        }
    }

    fun initialVariables() {

        btn_pic_location = findViewById(R.id.btn_pic_location)

        mapFragment = this.supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }


    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap
        mMap?.setOnMapClickListener { point ->
            Toast.makeText(
                this@MapsActivity,
                "" + point.latitude + ", " + point.longitude,
                Toast.LENGTH_SHORT
            ).show()

            mMap?.clear()
            longi = point.longitude
            lat = point.latitude
            Helper.pointLongitude = point.longitude
            Helper.pointLatitude = point.latitude

            val latLng = LatLng(point.latitude, point.longitude)
//        LatLng latLng = new LatLng(36.179302, -86.730911);
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            markerOptions.position(latLng)
            //move map camera
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            mMap?.animateCamera(CameraUpdateFactory.zoomTo(11f))
            mMap?.addMarker(markerOptions)


        }
//
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this@MapsActivity?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }
//
//
        longi = Helper.longitude
        lat = Helper.latitude

        val latLng = LatLng(Helper.latitude, Helper.longitude)
//        LatLng latLng = new LatLng(36.179302, -86.730911);
        val markerOptions = MarkerOptions()
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        markerOptions.position(latLng)
        //move map camera
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(11f))
        mMap?.addMarker(markerOptions)

//        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter())
    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = this@MapsActivity?.let {
            GoogleApiClient.Builder(it)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
        }
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (this@MapsActivity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity!!)
            try {
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(this@MapsActivity, "connection suspended", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this@MapsActivity, "connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onLocationChanged(location: Location?) {

        mLastLocation = location!!
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        Helper.pointLongitude = location.longitude
        Helper.pointLatitude = location.latitude
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        //move map camera
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))

        //stop location updates
        if (mGoogleApiClient != null) {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
        }
    }

}
