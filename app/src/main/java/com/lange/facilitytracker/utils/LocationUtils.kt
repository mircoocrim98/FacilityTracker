package com.lange.facilitytracker.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lange.facilitytracker.data.model.GeoData

object LocationUtils {
    const val REQUEST_LOCATION_PERMISSION = 1

    fun requestLocationPermission(fragment: Fragment) {
        val context = fragment.context ?: return
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fragment.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            getLocation(fragment, object : LocationCallback {
                override fun onLocationResult(geoData: GeoData) {
                    // Handle the GeoData result here
                }

                override fun onLocationError() {
                    // Handle the error here
                }
            })
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(fragment: Fragment, callback: LocationCallback) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(fragment.requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    callback.onLocationResult(GeoData(latitude, longitude))
                } ?: run {
                    Toast.makeText(fragment.context, "Standort nicht verfügbar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(fragment.context, "Fehler beim Abrufen des Standorts", Toast.LENGTH_SHORT).show()
            }
    }

    interface LocationCallback {
        fun onLocationResult(geoData: GeoData)
        fun onLocationError()
    }
}
