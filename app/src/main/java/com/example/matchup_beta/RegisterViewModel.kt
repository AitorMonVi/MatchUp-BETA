package com.example.matchup_beta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RegisterFormState(
    val isValid: Boolean = false,
    val usernameError: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val currentPasswordError: String? = null
)

class RegisterViewModel : ViewModel() {

    private val _formState = MutableLiveData(RegisterFormState())
    val formState: LiveData<RegisterFormState> = _formState

    private var currentUsername = ""
    private var currentEmail = ""
    private var currentPassword = ""
    private var currentConfirmPassword = ""
    private var currentName = ""

    fun updateUsername(username: String) {
        currentUsername = username.trim()
        validateForm()
    }

    fun updateEmail(email: String) {
        currentEmail = email.trim()
        validateForm()
    }

    fun updatePassword(password: String) {
        currentPassword = password
        validateForm()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        currentConfirmPassword = confirmPassword
        validateForm()
    }

    fun updateName(name: String) {
        currentName = name.trim()
        validateForm()
    }

    private fun validateForm() {
        val usernameError = if (!validateUsername(currentUsername)) "Nombre de usuario no válido" else null
        val nameError = if (currentName.isBlank()) "Campo requerido" else null
        val emailError = if (!validateEmail(currentEmail)) "Correo electrónico no válido" else null
        val passwordError = if (!validatePassword(currentPassword)) "Contraseña débil" else null
        val confirmPasswordError = if (currentPassword != currentConfirmPassword) "Las contraseñas no coinciden" else null
        val nameValid = currentName.isNotBlank()

        val isValid = listOf(
            usernameError,
            nameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).all { it == null } && nameValid

        _formState.value = RegisterFormState(
            usernameError = usernameError,
            nameError = nameError,
            emailError = emailError,
            passwordError = passwordError,
            currentPasswordError = confirmPasswordError,
            isValid = isValid
        )

    }

    private fun validatePassword(password: String): Boolean {
        val regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}$")
        return regex.matches(password)
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateUsername(username: String): Boolean {
        val regex = Regex("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")
        return regex.matches(username)
    }
}