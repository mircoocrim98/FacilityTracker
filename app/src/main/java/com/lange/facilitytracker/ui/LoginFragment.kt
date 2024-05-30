package com.lange.facilitytracker.ui

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lange.facilitytracker.MainViewModel
import com.lange.facilitytracker.data.model.LoginRequest
import com.lange.facilitytracker.data.model.User
import com.lange.facilitytracker.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hidePassword = true

        binding.btnLogin.setOnClickListener {
            val mail = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()


            viewModel.login(LoginRequest(mail, password))
            viewModel.loginResponse.observe(viewLifecycleOwner) {
                if (it.code()==200) {
                    if (binding.checkBox.isChecked) {
                        val sharedPreferences =
                            requireActivity().getSharedPreferences("FTracker", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("sessionToken", it.body()?.authentication?.sessionToken)
                        editor.apply()
                        viewModel.currentUserId = it.body()?._id
                    }
                    val navController = findNavController()
                    val direction = LoginFragmentDirections.toOverviewFragment()
                    navController.navigate(direction)
                } else if (it.code() == 400)  {
                    Toast.makeText(context, "Wrong Email or password", Toast.LENGTH_LONG).show()
                }


            }


        }

        binding.ivVisibleOnOff.setOnClickListener {
            if (hidePassword) {
                binding.etPasswordLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                hidePassword = false
            } else {
                binding.etPasswordLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hidePassword = true
            }
        }

        binding.tvSignUp.setOnClickListener {
            val navController = findNavController()
            val direction = LoginFragmentDirections.toRegisterFragment()
            navController.navigate(direction)
        }
    }
}