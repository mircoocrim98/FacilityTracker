package com.lange.facilitytracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.R
import com.lange.facilitytracker.data.adapter.TaskAdapter
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.databinding.FragmentUpdatetodoBinding

class UpdateTodoFragment: Fragment() {
    private lateinit var binding: FragmentUpdatetodoBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdatetodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tasks = viewModel.currentJob.value?.jobs_done?.toMutableList()
        var job: Job?

        binding.recyclerView.adapter = tasks?.let { TaskAdapter(it, viewModel) }

        binding.button.setOnClickListener {
            job = Job(
                viewModel.currentJob.value?._id, listOf(viewModel.currentUserId), null, null, null, null, System.currentTimeMillis(), null, GeoData(0.0, 0.0), null
            )
            viewModel.currentJob.value?._id?.let { jobId ->
                if (job != null) {
                    Log.d("Job2", "job.toString()")
                    viewModel.updateJob(
                        jobId,
                        job!!
                    )
                }
            }
            if (job != null) {
                Log.d("Job", job.toString())
                viewModel.setCurrentJob(job!!)
            }
            val navController = findNavController()
            val direction = UpdateTodoFragmentDirections.actionUpdateTodoFragmentToTimetrackerFragment()
            navController.navigate(direction)
        }
    }
}