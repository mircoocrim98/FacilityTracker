package com.lange.facilitytracker.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.R
import com.lange.facilitytracker.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button2.setOnClickListener {
            val sharedPreferences =
                requireActivity().getSharedPreferences("FTracker", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("sessionToken", null)
            editor.apply()

            findNavController().navigate(R.id.loginFragment)
        }
    }

}