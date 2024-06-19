package com.lange.facilitytracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.TypeOfWorkEnum
import com.lange.facilitytracker.data.adapter.TaskAdapter
import com.lange.facilitytracker.data.model.GeoData
import com.lange.facilitytracker.data.model.Job
import com.lange.facilitytracker.data.model.Task
import com.lange.facilitytracker.databinding.FragmentStartjobBinding
import retrofit2.Response

class StartjobFragmernt: Fragment() {
    private lateinit var binding: FragmentStartjobBinding
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var taskAdapter: TaskAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartjobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskAdapter = TaskAdapter(mutableListOf(),viewModel)

        with(binding) {
            recyclerView.adapter = taskAdapter

            val tasksList: MutableList<Task> = mutableListOf()
            for (i in viewModel.taskResources.value!!){
                tasksList.add(Task(i.text))
            }

            taskAdapter.addItems(tasksList)

            btnStartJob.setOnClickListener {
                val checkedItems = taskAdapter.getCheckedItems()
                val startTime = System.currentTimeMillis()

                val job = Job(
                    _id = null,
                    id_user = listOf(viewModel.currentUserId),
                    id_creator = viewModel.currentUserId,
                    id_address = viewModel.currentAddress.value?._id,
                    job_type = when (viewModel.currentTypeOfWork) {
                        TypeOfWorkEnum.cleaning -> 0
                        TypeOfWorkEnum.maintenance -> 2
                        TypeOfWorkEnum.damagereport -> 4
                        null -> Log.e("StartJobFragment", "TypeOfWork is null")
                    },
                    jobs_done = checkedItems,
                    startTime = startTime,
                    endTime = null,
                    startLocation = viewModel.geoData,
                    endLocation = null,
                )
                viewModel.createJob(job)
                viewModel.createJobResponse.observe(viewLifecycleOwner) {

                    if (!it.isSuccessful){
                        Log.e("StartJobFragment", it.errorBody().toString())
                    }
                    Log.i("StartJobFragment", it.body().toString())

                    if (it.body() != null){
                        viewModel.setCurrentJob(it.body()!!)

                        //navigate to timetracker
                        val navController = findNavController()
                        val direction = StartjobFragmerntDirections.actionStartjobFragmerntToTimetrackerFragment()
                        navController.navigate(direction)
                    }else{
                        Log.e("StartJobFragment", "body is null")
                    }

                }
            }

            btnAddTaskToList.setOnClickListener {
                val textToAdd = editTextText.text.toString()
                if (textToAdd.isNotBlank()){
                    taskAdapter.addItem(textToAdd)
                    editTextText.text.clear()
                }
            }
        }

    }
}