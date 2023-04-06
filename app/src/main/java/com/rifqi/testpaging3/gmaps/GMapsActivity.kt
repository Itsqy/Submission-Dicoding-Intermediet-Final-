package com.rifqi.testpaging3.gmaps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.rifqi.testpaging3.AuthViewModel
import com.rifqi.testpaging3.UserPrefferences
import com.rifqi.testpaging3.ViewModelFactory
import com.rifqi.testpaging3.login.dataStore
import com.rifqi.testpaging3.menu.MenuViewModel
import com.rifqi.testpaging3.R
import com.rifqi.testpaging3.databinding.ActivityGmapsBinding

class GMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityGmapsBinding

    private val menuViewModel: MenuViewModel by viewModels()
    private val boundsBuilder = LatLngBounds.Builder()
    private val authViewModel: AuthViewModel by viewModels() {
        ViewModelFactory(UserPrefferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGmapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        authViewModel.getUser().observe(this) {
            menuViewModel.setStory(it.userToken)
            Log.d("tokenUSer",it.userName.toString())
        }
        getMyLoc()
        setMapStyle()
        showAllStoryCoor()


    }


    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e("mapStyle", "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e("mapStyle", "Can't find style. Error: ", exception)
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLoc()
            }
        }

    private fun getMyLoc() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showAllStoryCoor() {
        menuViewModel.getStoryData.observe(this) { listStory ->
            for (it in listStory) {
                val latLng = LatLng(it.lat, it.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(it.name)
                        .snippet(it.description)
                )
                Log.d("valuelong", it.lon.toString())
                boundsBuilder.include(latLng)

            }
            val point = LatLng(-6.4996, 107.049)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 3f))
        }

    }

}