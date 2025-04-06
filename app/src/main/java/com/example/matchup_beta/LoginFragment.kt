package com.example.matchup_beta

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val emailEditText = view.findViewById<EditText>(R.id.email)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton= view.findViewById<Button>(R.id.enterButton)
        val registerButton = view.findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener{
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, ingresa email y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val retrofit = RetrofitService.MatchUPAPI.API()
            val loginRequest = LoginRequest(email = email, contrasena = password)
            println("Intentando login con: $loginRequest")
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = retrofit.login(loginRequest)
                    Log.d("LoginFragment", "Código de respuesta: ${response.code()}")
                    Log.d("LoginFragment", "Cuerpo de la respuesta: ${response.body()}")
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        Log.d("LoginFragment", "Respuesta del login: $loginResponse")
                        val token = loginResponse?.access_token
                        if(token != null) {
                            val jwt = JWT(token)
                            val userId = jwt.getClaim("sub").asInt()
                            if(userId != null) {
                                val sharedPref = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putInt("userId", userId)
                                    putString("token", token)
                                    apply()
                                }
                                (activity as? MainActivity)?.let { mainActivity ->
                                    mainActivity.replaceFragment(ScrollFragment(), "")
                                    mainActivity.showToolbarAndDrawer()
                                }
                            } else {
                                Toast.makeText(requireContext(), "Error: No se pudo extraer el ID del usuario", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Error: No se recibió un token válido", Toast.LENGTH_SHORT).show()
                        }
                    }  else {
                        Toast.makeText(requireContext(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        registerButton.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(RegisterFragment(), "Crear cuenta")
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