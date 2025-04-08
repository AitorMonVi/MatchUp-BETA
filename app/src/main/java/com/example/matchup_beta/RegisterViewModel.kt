package com.example.matchup_beta

import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    fun validatePassword(password: String): Boolean {
        val regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}$")
        return regex.matches(password)
    }
    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}