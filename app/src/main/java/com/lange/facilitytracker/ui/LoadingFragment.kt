package com.lange.facilitytracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.TypeOfWorkEnum

import com.lange.facilitytracker.data.model.AuthenticationPayload
import com.lange.facilitytracker.data.model.TaskResources
import com.lange.facilitytracker.databinding.FragmentLoadingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LoadingFragment : Fragment() {
    private lateinit var binding: FragmentLoadingBinding
    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        viewModel.getAllTasksFromDB()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("FTracker", AppCompatActivity.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("sessionToken",null)



        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)

            if (viewModel.taskResources.value.isNullOrEmpty()){
                viewModel.insertAllTasksInDB(
                    listOf(
                        TaskResources(text = "Treppenhausreinigung", taskType = 0),
                        TaskResources(text = "Treppengeländerreinigung", taskType = 0),
                        TaskResources(text = "Fensterreinigung", taskType = 0),
                        TaskResources(text = "Speermüllentsorgung", taskType = 0),
                        TaskResources(text = "Mülltonnenservice", taskType = 0),
                        TaskResources(text = "Fenster einstellen", taskType = 1),
                        TaskResources(text = "Durchlauferhitzer austauschen", taskType = 1),
                        TaskResources(text = "Feuchtigkeitsbeseitigung", taskType = 1),
                        TaskResources(text = "Streichen", taskType = 1),
                        TaskResources(text = "Feuchtigkeitsschaden", taskType = 2),
                        TaskResources(text = "Glasbruch", taskType = 2),
                        TaskResources(text = "Klingelanlage defekt", taskType = 2),
                        TaskResources(text = "Graffitis", taskType = 2)
                    )
                )
            }

            if (sessionToken == null) {
                val navController = findNavController()
                val direction = LoadingFragmentDirections.toLoginFragment()
                navController.navigate(direction)
            }

            if (sessionToken != null) {
                viewModel.sendToken(AuthenticationPayload(sessionToken))
            }

            viewModel.loginResponse.observe(viewLifecycleOwner) {
                if (it.code()==200){
                    val navController = findNavController()
                    val direction = LoadingFragmentDirections.toOverviewFragmentWithoutLogin()
                    navController.navigate(direction)
                } else {
                    val navController = findNavController()
                    val direction = LoadingFragmentDirections.toLoginFragment()
                    navController.navigate(direction)
                }
            }
        }
    }
}