package com.lange.facilitytracker.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.adapter.OverviewAdapter
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
            val jobs = it.filter { it.job_type in listOf(1,3,5) }
            binding.rvJobsdone.adapter = OverviewAdapter(jobs, viewModel, viewLifecycleOwner)
        }
    }
}