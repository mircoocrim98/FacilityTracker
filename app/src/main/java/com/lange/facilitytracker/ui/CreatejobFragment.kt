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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.TypeOfWorkEnum
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.databinding.FragmentCreatejobBinding

class CreatejobFragment : Fragment() {
    private lateinit var binding: FragmentCreatejobBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.destinationTodo = false
        binding = FragmentCreatejobBinding.inflate(inflater, container, false)
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


        val buttons = listOf(binding.btnClean, binding.btnMaintenance, binding.btnDamage)
        buttons.forEach {
            it.setOnClickListener {btn ->
                when (btn) {
                    binding.btnClean -> {
                        viewModel.currentTypeOfWork = TypeOfWorkEnum.cleaning
                        viewModel.getAllTasksByTypeOfWork(0)
                    }
                    binding.btnMaintenance -> {
                        viewModel.currentTypeOfWork = TypeOfWorkEnum.maintenance
                        viewModel.getAllTasksByTypeOfWork(1)
                    }
                    binding.btnDamage -> {
                        viewModel.currentTypeOfWork = TypeOfWorkEnum.damagereport
                        viewModel.getAllTasksByTypeOfWork(2)
                    }
                    else -> null
                }
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation(object : LocationCallback {
                        override fun onLocationResult(geoData: GeoData) {
                            viewModel.getAdressByGeoData(geoData)
                            viewModel.geoData = geoData
                    }
                        override fun onLocationError() {
                        // Handle the error here
                        }
                    })
                }  else {
                    requestLocationPermission()
                }
                binding.loadingCardview.visibility = View.VISIBLE
                viewModel.nearbyAddresses.observe(viewLifecycleOwner){
                    if (it.code()==200){
                        val navController = findNavController()
                        val direction = CreatejobFragmentDirections.toLocationPicker()
                        navController.navigate(direction)
                    } else {
                        Toast.makeText(context, "No managed property found near your location", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}