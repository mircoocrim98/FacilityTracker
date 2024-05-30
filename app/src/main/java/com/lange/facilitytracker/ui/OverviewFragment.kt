package com.lange.facilitytracker.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }



    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(object : LocationCallback {
                    override fun onLocationResult(geoData: GeoData) {
                        // Handle the GeoData result here
                    }

                    override fun onLocationError() {
                        // Handle the error here
                    }
                })

            } else {
                // Berechtigung verweigert
                Toast.makeText(context, "Standortberechtigung verweigert", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun requestLocationPermission() {
        context?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            } else {
                // Berechtigung bereits erteilt
                getLocation(object : LocationCallback {
                    override fun onLocationResult(geoData: GeoData) {
                        // Handle the GeoData result here
                    }

                    override fun onLocationError() {
                        // Handle the error here
                    }
                })

            }
        }
    }

    interface LocationCallback {
        fun onLocationResult(geoData: GeoData)
        fun onLocationError()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(callback: LocationCallback) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    // Logik zum Verarbeiten des Standortobjekts
                    val latitude = it.latitude
                    val longitude = it.longitude
                    Log.i("test","Lat: $latitude, Long: $longitude")
                    Toast.makeText(context, "Lat: $latitude, Long: $longitude", Toast.LENGTH_LONG).show()
                    callback.onLocationResult(GeoData(latitude, longitude))

                } ?: run {
                    // Falls der Standort null ist
                    Toast.makeText(context, "Standort nicht verfügbar", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Fehler beim Abrufen des Standorts", Toast.LENGTH_SHORT).show()
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation(object : LocationCallback {
                    override fun onLocationResult(geoData: GeoData) {
                        viewModel.getAdressByGeoData(geoData)
                    }

                    override fun onLocationError() {
                        // Handle the error here
                    }
                })


                viewModel.nearbyAddresses.observe(viewLifecycleOwner){
                    Log.i("####", it.body()?.size.toString())
                }
            } else {
                requestLocationPermission()
            }
        }

    }
}