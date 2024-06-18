package com.lange.facilitytracker

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lange.facilitytracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.overviewFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.toDoFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.createjobFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                R.id.settingsFragment -> binding.bottomNavigationView.visibility = View.VISIBLE
                else -> binding.bottomNavigationView.visibility = View.GONE
            }
        }

//        viewModel.errorMessage.observe(this){
//            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
//        }
    }
}