package com.lange.facilitytracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.adapter.LocationAdapter
import com.lange.facilitytracker.databinding.FragmentLocationpickerBinding

class LocationpickerFragment : Fragment() {

    private lateinit var binding: FragmentLocationpickerBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationpickerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nearbyAddresses.observe(viewLifecycleOwner){ addresses ->
            binding.rvAddresses.adapter = addresses.body()?.let { LocationAdapter(it, viewModel) }
        }
    }
}