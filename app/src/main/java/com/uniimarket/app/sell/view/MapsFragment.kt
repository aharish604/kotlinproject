package com.uniimarket.app.sell.view

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
import com.uniimarket.app.Dashboard.view.DashboardActivity
import com.uniimarket.app.Helper
import com.uniimarket.app.R

class MapsFragment : Fragment(), OnMapReadyCallback,
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater?.inflate(R.layout.map_fragment, container, false)

        initialVariables(view)

        var sharedPref: SharedPreferences? = context?.getSharedPreferences("uniimarket", 0)
        (context as DashboardActivity).imv_profile?.let { Glide.with(this)
            .load(sharedPref?.getString("profilepic", null))
            .placeholder(R.drawable.profile).into(it) }


//        var autocompleteFragment: AutocompleteSupportFragment =
//            this.childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))
//
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener,
//            com.google.android.libraries.places.widget.listener.PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                Log.i("place full", "Place: " + place?.name + ", " + place?.id)
//            }
//
//            override fun onPlaceSelected(place: com.google.android.gms.location.places.Place?) {
//                Log.i("place", "Place: " + place?.name + ", " + place?.id)
//            }
//
//            override fun onError(status: Status) {
//                // TODO: Handle the error.
//                Log.i("place", "An error occurred: " + status)
//            }
//        })


//        mMap?.setOnMarkerDragListener(OnMarkerDragListener() {
//
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//                // TODO Auto-generated method stub
//                // Here your code
//                Toast.makeText(
//                    MainActivity.this, "Dragging Start",
//                    Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                // TODO Auto-generated method stub
//                Toast.makeText(
//                    MainActivity.this,
//                    "Lat " + map.getMyLocation().getLatitude() + " "
//                            + "Long " + map.getMyLocation().getLongitude(),
//                    Toast.LENGTH_LONG).show();
//                System.out.println("yalla b2a "
//                        + map.getMyLocation().getLatitude());
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//                // TODO Auto-generated method stub
//                // Toast.makeText(MainActivity.this, "Dragging",
//                // Toast.LENGTH_SHORT).show();
//                System.out.println("Draagging");
//            }
//        });

        return view
    }

    fun initialVariables(view: View?) {

        btn_pic_location = view?.findViewById(R.id.btn_pic_location)

        mapFragment = this.childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap
        mMap?.setOnMapClickListener { point ->
            Toast.makeText(
                context,
                "" + point.latitude + ", " + point.longitude,
                Toast.LENGTH_SHORT
            ).show()

            mMap?.clear()
            longi = point.longitude
            lat = point.latitude

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
            if (context?.let {
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
        mGoogleApiClient = this!!.context?.let {
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
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
            try {
                mFusedLocationClient?.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Toast.makeText(context, "Connection suspended", Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show()
    }

    override fun onLocationChanged(location: Location?) {

        mLastLocation = location!!
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        //Place current location marker
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
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
