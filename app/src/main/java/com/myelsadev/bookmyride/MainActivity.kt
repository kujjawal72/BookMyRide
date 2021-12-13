package com.myelsadev.bookmyride

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.libraries.places.api.Places
import com.google.android.material.navigation.NavigationBarView
import com.myelsadev.bookmyride.base.BaseActivity
import com.myelsadev.bookmyride.databinding.ActivityMainBinding
import com.myelsadev.bookmyride.ext.addFragment
import com.myelsadev.bookmyride.ext.replaceFragment
import com.myelsadev.bookmyride.viewmodel.MapViewModel
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.util.*

@RuntimePermissions
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayout(inflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflater)

    private lateinit var locationManager: LocationManager

    private lateinit var mViewModel: MapViewModel
    override fun bindViews(savedInstanceState: Bundle?) {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key), Locale.getDefault());
        }
        mViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        mViewModel.requestCurrentLocation.observe(this,{
            if (it){
                getLocationWithPermissionCheck()
            }
        })

        binding.dashboardBottomNav.setOnItemSelectedListener(
            mOnBottomNavigationViewItemSelectedListener
        )
        binding.dashboardBottomNav.setOnItemReselectedListener {
            //--- do nothing ---
        }
        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, BookRideFragment.newInstance())
        }
    }

    private val mOnBottomNavigationViewItemSelectedListener =
        NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_book_rides -> {
                    replaceFragment(R.id.fragment_container, BookRideFragment.newInstance())
                }
                R.id.bottom_nav_my_rides -> {
                    replaceFragment(R.id.fragment_container, MyRidesFragment.newInstance())
                }
            }
            true
        }


    @NeedsPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (hasGps) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 30*1000L, 0F,
                object : LocationListener {
                    override fun onLocationChanged(p0: Location) {
                        onLocationFetched(p0)
                    }

                    override fun onProviderDisabled(provider: String) {

                    }

                    override fun onProviderEnabled(provider: String) {

                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }
                })

        } else {
            onLocationNotEnabled()
        }
    }

    private fun onLocationFetched(location: Location) {
        val geo = Geocoder(applicationContext, Locale.getDefault())
        val addresses = geo.getFromLocation(location.latitude, location.longitude, 1)
        mViewModel.setCurrentLocation(LocationModel(location.latitude,location.longitude,addresses[0].getAddressLine(0)))
    }

    private var temp = 0

    private fun onLocationNotEnabled() {
        MaterialDialog(this)
            .title(text = "Location not enabled")
            .message(text = "Please enable the location otherwise the this service will not work.")
            .positiveButton(text = "Enable Location") {
                it.dismiss()
                temp = 1
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }.negativeButton(text = "Go, Back") {
                it.dismiss()
                onBackPressed()
            }.cancelOnTouchOutside(false)
            .cancelable(false)
            .show()
    }


    @OnPermissionDenied(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun permissionNotGranted(){
        MaterialDialog(this)
            .title(text = "Permission not granted")
            .message(text = "Please allow the permissions otherwise the this service will not work.")
            .positiveButton(text = "Allow again") {
                it.dismiss()
                getLocationWithPermissionCheck()
            }.negativeButton(text = "Go, Back") {
                it.dismiss()
                onBackPressed()
            }.cancelOnTouchOutside(false)
            .cancelable(false)
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}