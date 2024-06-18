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
import com.lange.facilitytracker.data.adapter.OverviewAdapter
import com.lange.facilitytracker.data.adapter.ToDoAdapter
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.jobsById.observe(viewLifecycleOwner){
            val jobs = it.body()?.filter { it.job_type == 1 || it.job_type == 3 || it.job_type == 5}
            binding.rvJobsdone.adapter = jobs?.let { OverviewAdapter(it, viewModel, viewLifecycleOwner) }
        }
    }
}