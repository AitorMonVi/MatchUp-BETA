package com.example.matchup_beta

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var nameText : EditText
    private lateinit var ageText : EditText
    private lateinit var descriptionText : EditText
    private lateinit var editProfile : ImageButton
    private lateinit var makeChanges : Button

    private var click : Boolean = false
    private lateinit var nombre : String
    private lateinit var edad : String
    private lateinit var descripcion : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // declaramos botones, text, etc
        nameText = view.findViewById(R.id.name)
        ageText = view.findViewById(R.id.age)
        descriptionText = view.findViewById(R.id.description)
        editProfile = view.findViewById(R.id.edit_profile)
        makeChanges = view.findViewById(R.id.make_changes)

        val sharedPref = requireActivity().getSharedPreferences("UserData", 0)
        val userId = sharedPref.getInt("userId", -1)

        val retrofit = RetrofitService.MatchUPAPI.API()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val userInfo = retrofit.getUser(userId)
                if (userInfo.isSuccessful) {
                    val user = userInfo.body()
                    nameText.setText(user?.name ?: "Nombre no disponible")
                    ageText.setText(user?.age?.toString() ?: "Edad no disponible")
                    descriptionText.setText(user?.status ?: "Descripcion no disponible")

                    nombre = nameText.text.toString()
                    edad = ageText.text.toString()
                    descripcion = descriptionText.text.toString()
                } else {
                    Toast.makeText(context, "Error fetching user info: ${userInfo.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("ProfileFragment", "Exception: ${e.message}", e)
            }
        }

        // hacemos invisible el boton para hacer cambios
        makeChanges.visibility = View.INVISIBLE
        nameText.isEnabled = false
        ageText.isEnabled = false
        descriptionText.isEnabled = false

        editProfile.setOnClickListener{
            if (!click) {
                changeValues(true)
            } else {
                changeValues(false)
                modifyEditText()
            }
        }

        makeChanges.setOnClickListener{
            changeValues(false)

            val name = nameText.text.toString().trim()
            val age = ageText.text.toString().trim()
            val status = descriptionText.text.toString().trim()

            if (name.isEmpty() || status.isEmpty() || age.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, no dejes campos vacios", Toast.LENGTH_SHORT).show()
                modifyEditText()
                return@setOnClickListener
            }

            val ageInt = age.toIntOrNull()

            if (ageInt == null) {
                Toast.makeText(requireContext(), "Por favor, ingrese un número válido para la edad", Toast.LENGTH_SHORT).show()
                modifyEditText()
                return@setOnClickListener
            }

            if (ageInt < 18) {
                Toast.makeText(requireContext(), "Esta aplicación no es para menores de edad", Toast.LENGTH_SHORT).show()
                modifyEditText()
                return@setOnClickListener
            }
            // los cambios son correctos seteamos los valores editados como correctos
            nombre = name
            edad = age
            descripcion = status

            val user = UserUpdate(
                name = nombre,
                status = descripcion,
                image = "",
                age = ageInt
            )

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response = retrofit.updateUser(user)
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Usuario modificado correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error al modificar usuario: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Excepción: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return view
    }

    private fun changeValues(value : Boolean) {
        if (value) makeChanges.visibility = View.VISIBLE
        else makeChanges.visibility = View.INVISIBLE
        nameText.isEnabled = value
        ageText.isEnabled = value
        descriptionText.isEnabled = value
        click = value
    }

    public fun modifyEditText() {
        nameText.setText(nombre)
        ageText.setText(edad)
        descriptionText.setText(descripcion)
    }
}
