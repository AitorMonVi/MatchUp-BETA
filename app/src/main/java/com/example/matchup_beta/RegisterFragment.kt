package com.example.matchup_beta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.editTextName)
        val lastnameEditText = view.findViewById<EditText>(R.id.editTextLastname)
        val emailEditText = view.findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = view.findViewById<EditText>(R.id.editTextPassword)

        val registerButton = view.findViewById<Button>(R.id.registerButton)
        val alreadyAccountButton = view.findViewById<Button>(R.id.alreadyAccountButton)

        registerButton.setOnClickListener {
            val nombre = nameEditText.text.toString().trim()
            val apellido = lastnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val contrasena = passwordEditText.text.toString().trim()

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = UserCreate(
                nombre = nombre,
                apellido = apellido,
                email = email,
                contrasena = contrasena
            )
            val retrofit = RetrofitService.MatchUPAPI.API()

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = retrofit.createUser(usuario)
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                        (activity as? MainActivity)?.replaceFragment(LoginFragment(), "Login")
                    } else {
                        Toast.makeText(requireContext(), "Error al crear usuario: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Excepción: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        alreadyAccountButton.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(LoginFragment(), "Login")
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = PreferencesManager(requireContext())

        lifecycleScope.launch {
            preferencesManager.incrementFragmentEntries()
        }
    }
}
