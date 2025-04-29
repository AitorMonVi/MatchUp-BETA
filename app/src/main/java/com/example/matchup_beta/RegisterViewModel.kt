package com.example.matchup_beta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class RegisterFormState(
    val isValid: Boolean = false,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val currentPasswordError: String? = null
)

class RegisterViewModel: ViewModel() {

    private val _formState = MutableLiveData<RegisterFormState>(RegisterFormState())
    val formState: LiveData<RegisterFormState> = _formState

    private var currentUsername: String = ""
    private var currentEmail: String = ""
    private var currentPassword: String = ""
    private var currentConfirmPassword: String = ""


    fun updateUsername(username: String) {
        currentUsername = username.trim()

        val usernameError = if (!validateUsername(username)) "Nombre de usuario no válido" else null
        val emailValid = validateEmail(currentEmail)
        val passwordValid = validatePassword(currentPassword)
        val isFormValid = usernameError == null && emailValid && passwordValid

        val currentState = _formState.value ?: RegisterFormState()
        _formState.value = currentState.copy(
            usernameError = usernameError,
            isValid = isFormValid
        )
    }

    fun updateEmail(email: String) {
        currentEmail = email

        val emailError = if (!validateEmail(email)) "Introduce el correo electrónico en el formato someone@example.com" else null
        val usernameValid = validateUsername(currentUsername)
        val passwordValid = validatePassword(currentPassword)
        val isFormValid = emailError == null && usernameValid && passwordValid

        val currentState = _formState.value ?: RegisterFormState()
        _formState.value = currentState.copy(
            emailError = emailError,
            isValid = isFormValid
        )
    }

    fun updatePassword(password: String) {
        currentPassword = password

        val passwordError = if (!validatePassword(password)) "La contraseña debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un símbolo" else null
        val usernameValid = validateUsername(currentUsername)
        val emailValid = validateEmail(currentEmail)
        val isFormValid = passwordError == null && usernameValid && emailValid

        val currentState = _formState.value ?: RegisterFormState()
        _formState.value = currentState.copy(
            passwordError = passwordError,
            isValid = isFormValid
        )
    }

    fun updateConfirmPassword(confirmPassword: String) {
        currentConfirmPassword = confirmPassword

        val confirmPasswordError = if (currentPassword != confirmPassword) "Las contraseñas no coinciden" else null
        val usernameValid = validateUsername(currentUsername)
        val emailValid = validateEmail(currentEmail)
        val passwordValid = validatePassword(currentPassword)
        val isFormValid = confirmPasswordError == null && usernameValid && emailValid && passwordValid

        val currentState = _formState.value ?: RegisterFormState()
        _formState.value = currentState.copy(
            currentPasswordError = confirmPasswordError,
            isValid = isFormValid
        )
    }



    fun validatePassword(password: String): Boolean {
        val regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}$")
        return regex.matches(password)
    }
    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun validateUsername(username: String): Boolean {
        val regex = Regex("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")
        return regex.matches(username)
    }
}