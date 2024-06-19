package com.lange.facilitytracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.R
import com.lange.facilitytracker.TypeOfWorkEnum
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.databinding.FragmentCreatejobBinding
import com.lange.facilitytracker.utils.LocationUtils

class CreatejobFragment : Fragment() {
    private lateinit var binding: FragmentCreatejobBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.destinationTodo = false
        binding = FragmentCreatejobBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttons = listOf(binding.btnClean, binding.btnMaintenance, binding.btnDamage)
        buttons.forEach {
            it.setOnClickListener { btn ->
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
                }
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationUtils.getLocation(this, object : LocationUtils.LocationCallback {
                        override fun onLocationResult(geoData: GeoData) {
                            viewModel.getAdressByGeoData(geoData)
                            viewModel.geoData = geoData
                        }

                        override fun onLocationError() {
                            // Handle the error here
                        }
                    })
                } else {
                    LocationUtils.requestLocationPermission(this)
                }
                binding.loadingCardview.visibility = View.VISIBLE
                viewModel.nearbyAddresses.observe(viewLifecycleOwner) {
                    if (it.code() == 200) {
                        val navController = findNavController()
                        navController.navigate(R.id.locationpickerFragment)
                    } else {
                        Toast.makeText(context, "No managed property found near your location", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
