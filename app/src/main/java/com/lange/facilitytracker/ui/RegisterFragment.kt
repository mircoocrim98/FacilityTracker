package com.lange.facilitytracker.ui

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
import com.lange.facilitytracker.data.model.RegisterRequest
import com.lange.facilitytracker.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(){
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hidePassword = true

        binding.btnRegister.setOnClickListener {
            val mail = binding.etEmailRegister.text.toString()
            val username = binding.etUsernameRegister.text.toString()
            val password = binding.etPasswordRegister.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (password == confirmPassword) {
                viewModel.register(RegisterRequest(username,mail,password))
                viewModel.registerResponse.observe(viewLifecycleOwner){
                    if (it.code() == 200){
                        val navController = findNavController()
                        navController.navigateUp()
                    } else if (it.code() == 400) {
                        Toast.makeText(context, "This email address is already in use", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(context, "Password do not match", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvSignIn.setOnClickListener {
            val navController = findNavController()
            navController.navigateUp()
        }

        binding.visibleOnOffRegister.setOnClickListener {
            if (hidePassword) {
                binding.etPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                hidePassword = false
            } else {
                binding.etPasswordRegister.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                hidePassword = true
            }
        }
    }

}
