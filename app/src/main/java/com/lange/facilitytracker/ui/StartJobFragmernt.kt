package com.lange.facilitytracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.adapter.TaskAdapter
import com.lange.facilitytracker.data.model.Task
import com.lange.facilitytracker.databinding.FragmentStartjobBinding

class StartJobFragmernt: Fragment() {
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
                for (item in checkedItems){
                    Log.i("TASK", "Name: ${item.key} Value: ${item.value}")
                }
                //stat job api call
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