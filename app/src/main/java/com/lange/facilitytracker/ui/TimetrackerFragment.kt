package com.lange.facilitytracker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.R
import com.lange.facilitytracker.TypeOfWorkEnum
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.databinding.FragmentTimetrackerBinding
import com.lange.facilitytracker.utils.LocationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimetrackerFragment: Fragment() {
    private lateinit var binding: FragmentTimetrackerBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimetrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startTime = viewModel.currentJob.value?.startTime
        var runningTimer = true
        CoroutineScope(Dispatchers.Main).launch {
            while (runningTimer) {
                if (startTime != null) {
                    viewModel.checkTimer(startTime)
                    delay(1000)
                }
            }

        }


        viewModel.timer.observe(viewLifecycleOwner){
            binding.timerTextView.text = it.toString()
        }

        var jobType = 0
        when (viewModel.currentTypeOfWork){
            TypeOfWorkEnum.cleaning -> jobType = 1
            TypeOfWorkEnum.maintenance -> jobType = 3
            TypeOfWorkEnum.damagereport -> jobType = 5
            null -> Log.e("TimetrackerFragment", "No job type selected")
        }

        binding.endJobButton.setOnClickListener {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationUtils.getLocation(this, object : LocationUtils.LocationCallback {
                    override fun onLocationResult(geoData: GeoData) {
                        viewModel.geoData = geoData
                    }

                    override fun onLocationError() {
                        // Handle the error here
                    }
                })
            } else {
                LocationUtils.requestLocationPermission(this)
            }
            viewModel.currentJob.value?._id?.let { it1 ->
                viewModel.updateJob(
                    it1,
                    com.lange.facilitytracker.data.model.Job(
                        viewModel.currentJob.value!!._id, null,
                        null, null, jobType, null, null, System.currentTimeMillis(), null, viewModel.geoData)
                )
                viewModel.setCurrentJob(
                    com.lange.facilitytracker.data.model.Job(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            }

            viewModel.updateJobResponse.observe(viewLifecycleOwner){
                if (it.code() == 200){
                    runningTimer = false
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1500)
                        val navController = findNavController()
                        val direction = if (viewModel.destinationTodo) R.id.toDoFragment else R.id.createjobFragment
                        navController.navigate(direction)
                    }
                } else {
                    Log.e("TimetrackerFragment", it.errorBody().toString())
                }
            }
        }
    }
}