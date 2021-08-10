package com.uniimarket.app.Dashboard.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.uniimarket.app.Dashboard.presenter.SearchPresenter
import com.uniimarket.app.Helper
import com.uniimarket.app.R
import com.uniimarket.app.categories.model.CategoriesTypeData
import com.uniimarket.app.chat.view.ChatFragment
import com.uniimarket.app.home.view.ProductsFragment
import com.uniimarket.app.more.MoreFragment
import com.uniimarket.app.more.profile.view.FullProfileFragment
import com.uniimarket.app.notification.view.NotificationFragment
import com.uniimarket.app.sell.view.SellFragment
import de.hdodenhof.circleimageview.CircleImageView


class DashboardActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener, SearchPresenter.SearchListener {


    var frame_layout: FrameLayout? = null
    var et_search: AutoCompleteTextView? = null

    var ll_home: RelativeLayout? = null
    var ll_notifications: LinearLayout? = null
    var ll_sell: LinearLayout? = null
    var ll_chat: LinearLayout? = null
    var ll_more: LinearLayout? = null
    var ll_back: LinearLayout? = null
    var imv_profile: CircleImageView? = null
    var ll_profile: LinearLayout? = null

    private var mMap: GoogleMap? = null
    internal var latitude: Double = 0.0
    internal var longitude: Double = 0.0
    private val PROXIMITY_RADIUS = 5000
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal var mLastLocation: Location? = null
    internal var mCurrLocationMarker: Marker? = null
    private var mLocationRequest: LocationRequest? = null
    var sharedPref: SharedPreferences? = null
    var searchPresenter: SearchPresenter? = null
    var imv_search: ImageView? = null
    var imv_user_search: ImageView? = null
    var ll_toolbar: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_dashboard)

        initialVariables()

        imv_user_search?.visibility = View.GONE
        imv_search?.visibility = View.VISIBLE

        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("chat")
            try {
                Log.e("NOTTTT", value + "")

                if (value == "CHAT") {
                    val firstFragment = ChatFragment()
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.add(R.id.frame_layout, firstFragment)
                    transaction.addToBackStack(true.toString())

                    transaction.commit()
                } else {
                    val firstFragment = NotificationFragment()
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.add(R.id.frame_layout, firstFragment)
                    transaction.addToBackStack(true.toString())

                    transaction.commit()
                }


            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                val firstFragment = NotificationFragment()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.add(R.id.frame_layout, firstFragment)
                transaction.addToBackStack(true.toString())

                transaction.commit()
            }
//            Toast.makeText(applicationContext, "NOTTTT" + value, Toast.LENGTH_LONG).show()
            // Get the text fragment instance

            //The key argument here must match that used in the other activity
        } else {
            // Get the text fragment instance
            val firstFragment = PathWaysFragment()
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.add(R.id.frame_layout, firstFragment)
            transaction.addToBackStack(true.toString())

            transaction.commit()
        }

        ll_back?.visibility = View.INVISIBLE
//        val firstFragment = HomeFragment()
//        firstFragment.arguments = intent.extras
//        val transaction = fragmentManager.beginTransaction()
//        transaction.add(R.id.frame_layout, firstFragment)
//        transaction.commit()


        imv_search?.setOnClickListener {
            Helper.edit = ""

            if (et_search?.text.toString().trim().isEmpty()) {

            } else {
                val inputMethodManager = getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), 0
                )
//                getSearchList(et_search?.text.toString().trim())


                Helper.searchProduct = et_search?.text.toString().trim()

//                val firstFragment = ProductsFragment()
                val firstFragment = SearchTabsFragment()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.add(R.id.frame_layout, firstFragment)
                transaction.addToBackStack(true.toString())
                transaction.commit()
            }

            hideKeyboard(currentFocus ?: View(this))

        }



        imv_profile?.let {
            Glide.with(this)
                .load(sharedPref?.getString("profilepic", null))
                .placeholder(R.drawable.profile).into(it)
        }

        if (mGoogleApiClient == null) {
            buildGoogleApiClient()
        }

        ll_home?.setOnClickListener {
            Helper.chatStop = false
            Helper.edit = ""
            replaceFragment(R.id.frame_layout, PathWaysFragment(), false, false)
        }

        ll_notifications?.setOnClickListener {
            Helper.chatStop = false
            replaceFragment(
                R.id.frame_layout,
                NotificationFragment(), false, false
            )
        }

        ll_profile?.setOnClickListener {
            Helper.chatStop = false
            Helper.edit = ""
            replaceFragment(
                R.id.frame_layout,
                FullProfileFragment(), false, false
            )
        }

        ll_sell?.setOnClickListener {
            Helper.chatStop = false
            Helper.edit = ""
//            Helper.pointLongitude = 0.0
//            Helper.pointLatitude = 0.0
            replaceFragment(R.id.frame_layout, SellFragment(), false, false)
        }

        ll_chat?.setOnClickListener {
            Helper.chatStop = false
            Helper.edit = ""
            replaceFragment(R.id.frame_layout, ChatFragment(), false, false)
        }

        ll_more?.setOnClickListener {
            Helper.chatStop = false
            Helper.edit = ""
            replaceFragment(R.id.frame_layout, MoreFragment(), false, false)
        }

    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getSearchList(productSearch: String) {
        searchPresenter?.getSearchProductList(productSearch)
    }

    private fun initialVariables() {
        frame_layout = findViewById<FrameLayout>(R.id.frame_layout)
        et_search = findViewById(R.id.et_search)
        sharedPref = getSharedPreferences("uniimarket", 0)
        ll_home = findViewById(R.id.ll_home)
        ll_notifications = findViewById(R.id.ll_notifications)
        ll_sell = findViewById(R.id.ll_sell)
        ll_chat = findViewById(R.id.ll_chat)
        ll_more = findViewById(R.id.ll_more)
        ll_back = findViewById(R.id.ll_back)
        imv_search = findViewById(R.id.imv_search)
        imv_user_search = findViewById(R.id.imv_user_search)
        imv_profile = findViewById(R.id.imv_profile)
        ll_profile = findViewById(R.id.ll_profile)
        ll_toolbar = findViewById(R.id.ll_toolbar)
        searchPresenter = SearchPresenter(this, this)

    }


    public fun replaceFragment(
        containerViewId: Int,
        fragment: Fragment,
        addStack: Boolean,
        isReplace: Boolean
    ) {
//
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(containerViewId, fragment)
//        transaction.addToBackStack(true.toString())

        transaction.commit()

    }

    public fun addFragment(
        containerViewId: Int,
        fragment: Fragment,
        addStack: Boolean,
        isReplace: Boolean
    ) {
//
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(containerViewId, fragment)
        transaction.addToBackStack(true.toString())

        transaction.commit()

    }

    fun removeFragment(fragment: Fragment, fragmentManager: FragmentManager?) {

        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.remove(fragment)
        fragmentTransaction?.commit()

//        onBackPressed()

//        val backEntry = fragmentManager
//            ?.getBackStackEntryAt(
//                fragmentManager
//                    ?.getBackStackEntryCount() - 1
//            )
//
//
//        Log.e("abt back", "remove")
//        val manager = fragmentManager
//        val transaction = manager?.beginTransaction()
//        manager?.backStackEntryCount
//        transaction?.remove(fragment)
//        transaction?.commit()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        //        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this@DashboardActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleApiClient()
                mMap?.setMyLocationEnabled(true)
            }
        } else {
            buildGoogleApiClient()
            mMap?.setMyLocationEnabled(true)
        }
        val criteria = Criteria()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(criteria, true)
        val location = locationManager.getLastKnownLocation(provider)
        val latitude1 = location.latitude
        val longitude1 = location.longitude
        //        LatLng latLng1 = new LatLng(latitude1, longitude1);
        //        Log.e("lat lng", latLng1.toString());
        //        latitude = latitude1;
        //        longitude = longitude1;
        try {
            Log.e("lat", latitude.toString())
            Log.e("lon", longitude.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            Log.e("lat1", latitude1.toString())
            Log.e("lon1", longitude1.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val latLng = LatLng(latitude, longitude)
        //        MarkerOptions markerOptions = new MarkerOptions();
        //        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        //        //move map camera
        //        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //                mMap.addMarker(new MarkerOptions()
        //                .position(latLng)
        //                .draggable(true)
        //                .title("Your Current Location"));

        //        onResume(latLng1);
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this@DashboardActivity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mGoogleApiClient?.connect()

    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest?.setInterval(1000)
        mLocationRequest?.setFastestInterval(1000)
        mLocationRequest?.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
        if (ContextCompat.checkSelfPermission(
                this@DashboardActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {

                LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mLocationRequest,
                    this@DashboardActivity as LocationListener
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location?) {
        Log.d("onLocationChanged", "entered")

        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker?.remove()
        }

        //Place current location marker
        latitude = location?.getLatitude()!!
        longitude = location?.getLongitude()
        val latLng = location?.getLatitude()?.let { LatLng(it, location?.getLongitude()) }
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
//        Toast.makeText(DashboardActivity.this, "Your Current Location", Toast.LENGTH_LONG).show();

        Log.e(
            "onLocationChanged act",
            String.format("latitude:%.3f longitude:%.3f", latitude, longitude)
        )
        Helper.latitude = latitude
        Helper.longitude = longitude

        Helper.pointLatitude = latitude
        Helper.pointLongitude = longitude

//        Helper.latitude = 36.166479;
//        Helper.longitude = -86.7734785;

        try {
            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
                Log.d("onLocationChanged", "Removing Location Updates")
            }
            Log.d("onLocationChanged", "Exit")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun searchResponseSuccess(
        status: String,
        message: String?,
        productDataList: ArrayList<CategoriesTypeData.Datum>
    ) {

        Helper.searchProductData = productDataList

        et_search?.setText("")
        val firstFragment = ProductsFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.frame_layout, firstFragment)
        transaction.addToBackStack(true.toString())
        transaction.commit()


    }


    override fun searchResponseFailure(status: String, message: String?) {

    }

}
