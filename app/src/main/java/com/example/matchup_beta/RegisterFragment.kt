package com.example.matchup_beta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val usernameEditText = view.findViewById<EditText>(R.id.editTextUsername)
        val nameEditText = view.findViewById<EditText>(R.id.editTextName)
        val emailEditText = view.findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.editTextConfirmPassword)
        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val alreadyAccountButton = view.findViewById<Button>(R.id.alreadyAccountButton)

        // Observa errores del ViewModel
        viewModel.formState.observe(viewLifecycleOwner) { state ->
            usernameEditText.error = state.usernameError
            nameEditText.error = state.nameError
            emailEditText.error = state.emailError
            passwordEditText.error = state.passwordError
            confirmPasswordEditText.error = state.currentPasswordError
            registerButton.isEnabled = state.isValid
        }

        // Actualiza el ViewModel al escribir
        usernameEditText.doAfterTextChanged { viewModel.updateUsername(it.toString()) }
        emailEditText.doAfterTextChanged { viewModel.updateEmail(it.toString()) }
        passwordEditText.doAfterTextChanged { viewModel.updatePassword(it.toString()) }
        confirmPasswordEditText.doAfterTextChanged { viewModel.updateConfirmPassword(it.toString()) }
        nameEditText.doAfterTextChanged { viewModel.updateName(it.toString()) }


        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val usuario = UserCreate(
                nombreUsuario = username,
                nombre = name,
                email = email,
                contrasena = password
            )

            val retrofit = RetrofitService.MatchUPAPI.API()

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = retrofit.createUser(usuario)
                    if (response.isSuccessful) {
                        navigateToLogin()
                    } else {
                        emailEditText.error = "Error al crear usuario"
                    }
                } catch (e: Exception) {
                    emailEditText.error = "Excepción: ${e.message}"
                }
            }
        }

        alreadyAccountButton.setOnClickListener {
            navigateToLogin()
        }

        return view
    }

    private fun navigateToLogin() {
        (activity as? MainActivity)?.replaceFragment(LoginFragment(), "Login")
    }
}
