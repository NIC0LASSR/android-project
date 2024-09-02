package com.example.clima

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class HomeScreen : AppCompatActivity() {

    // FusedLocationProviderClient is used to access the device's location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Request code for location permission
    private val requestCodeLocation = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_screen)

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Attempt to get the last known location
        getLastLocation()
    }

    // Function to get the last known location
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // Check if the necessary permissions are granted
        if (checkPermissions()) {
            // Check if location services (GPS/Network) are enabled
            if (isLocationEnabled()) {
                // Get the last known location
                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        // Request a new location if the last known location is not available
                        requestNewLocation()
                    } else {
                        // Delay for 2 seconds before starting MainActivity with the location data
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("lat", location.latitude.toString())
                            intent.putExtra("long", location.longitude.toString())
                            startActivity(intent)
                            finish() // Close the current activity
                        }, 2000)
                    }
                }
            } else {
                // Prompt the user to enable GPS
                Toast.makeText(this, "Por favor enciende tu GPS", Toast.LENGTH_LONG).show()
            }
        } else {
            // Request the necessary permissions if not already granted
            requestPermissions()
        }
    }

    // Function to request a new location if the last known location is not available
    @SuppressLint("MissingPermission")
    private fun requestNewLocation() {
        // Define the location request parameters
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1 // Request location updates only once
        }

        // Request location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    // Callback for location updates
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // Get the last location from the result
            val lastLocation: Location? = locationResult.lastLocation
            lastLocation?.let {
                // Start MainActivity with the latitude and longitude
                val intent = Intent(this@HomeScreen, MainActivity::class.java).apply {
                    putExtra("lat", it.latitude.toString())
                    putExtra("long", it.longitude.toString())
                }
                startActivity(intent)
                finish() // Close the current activity
            }
        }
    }

    // Check if location services (GPS/Network) are enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Check if the necessary location permissions are granted
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Request the necessary location permissions
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            requestCodeLocation
        )
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeLocation && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, attempt to get the last location
            getLastLocation()
        }
    }
}
