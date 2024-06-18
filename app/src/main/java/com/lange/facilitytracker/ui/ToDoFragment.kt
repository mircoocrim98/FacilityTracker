package com.lange.facilitytracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.adapter.ToDoAdapter
import com.lange.facilitytracker.databinding.FragmentTodoBinding

class ToDoFragment : Fragment() {
    private lateinit var binding: FragmentTodoBinding
    private val viewModel: MainViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.destinationTodo = true
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.jobsById.observe(viewLifecycleOwner) {
            val jobs = it.body()?.filter { it.job_type == 0 || it.job_type == 2 || it.job_type == 4}
            binding.rvTodo.adapter = jobs?.let { ToDoAdapter(it, viewModel, viewLifecycleOwner) }
        }
    }
}