package com.example.matchup_beta

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule

class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = RegisterViewModel()

    // username tests
    @Test
    fun `updateUsername devuelve error si el nombre de usuario está vacío`() {
        viewModel.updateUsername("")
        val result = viewModel.formState.value
        assertEquals("Nombre de usuario no válido", result?.usernameError)
        assertFalse(result?.isValid?: true)
    }
    @Test
    fun `updateUsername con menos de 8 caracteres devuelve error`() {
        viewModel.updateUsername("abc123")
        val result = viewModel.formState.value
        assertEquals("Nombre de usuario no válido", result?.usernameError)
        assertFalse(result?.isValid?:true)
    }
    @Test
    fun `updateUsername con caracteres inválidos devuelve error`() {
        viewModel.updateUsername( "__usuario")
        val result = viewModel.formState.value
        assertEquals("Nombre de usuario no válido", result?.usernameError)
        assertFalse(result?.isValid?:true)
    }
    @Test
    fun `updateUsername válido no da error`() {
        viewModel.updateUsername("usuario.valido")
        val result = viewModel.formState.value
        assertNull(result?.usernameError)
        assertFalse(result?.isValid ?: true)
    }
    @Test
    fun `updateUsername con espacios al principio y al final para probar el trim`() {
        viewModel.updateUsername(" usuario.valido   ")
        val result = viewModel.formState.value
        assertNull(result?.usernameError)
        assertFalse(result?.isValid ?: true)
    }
    @Test
    fun `updateUsername con 8 caracteres exactos es válido`() {
        viewModel.updateUsername("usuario1")
        val result = viewModel.formState.value
        assertNull(result?.usernameError)
        assertFalse(result?.isValid ?: true)
    }



    // email tests
    @Test
    fun `updateEmail con string vacío devuelve error`() {
        viewModel.updateEmail("")
        val result = viewModel.formState.value
        assertEquals("Introduce el correo electrónico en el formato someone@example.com", result?.emailError)
        assertFalse(result?.isValid?: true)
    }
    @Test
    fun `updateEmail con formato incorrecto devuelve error`() {
        viewModel.updateEmail("correo@invalido")
        val result = viewModel.formState.value
        assertNotNull(result?.emailError)
        assertFalse(result?.isValid ?: true)
    }
    @Test
    fun `updateEmail con formato válido no da error`() {
        viewModel.updateEmail("correo@email.com")
        val result = viewModel.formState.value
        assertNull(result?.emailError)
        assertFalse(result?.isValid?:true)
    }
    @Test
    fun `updateEmail primero inválido luego válido limpia el error`() {
        viewModel.updateEmail("correo@invalido")
        var result = viewModel.formState.value
        assertNotNull(result?.emailError)

        viewModel.updateEmail("correo@email.com")
        result = viewModel.formState.value
        assertNull(result?.emailError)
    }


    // password tests
    @Test
    fun `updatePassword con string vacío devuelve error`() {
        viewModel.updatePassword("")
        val result = viewModel.formState.value
        assertNotNull(result?.passwordError)
        assertFalse(result?.isValid?: true)
    }
    @Test
    fun `updatePassword sin símbolos devuelve error`() {
        viewModel.updatePassword("Password1")
        val result = viewModel.formState.value
        assertNotNull(result?.passwordError)
        assertFalse(result?.isValid?:true)
    }
    @Test
    fun `updatePassword sin números devuelve error`() {
        viewModel.updatePassword("Password!")
        val result = viewModel.formState.value
        assertNotNull(result?.passwordError)
        assertFalse(result?.isValid?:true)
    }
    @Test
    fun `updatePassword sin mayúsculas devuelve error`() {
        viewModel.updatePassword("password1!")
        val result = viewModel.formState.value
        assertNotNull(result?.passwordError)
        assertFalse(result?.isValid?:true)
    }
    @Test
    fun `updatePassword válida no da error`() {
        viewModel.updatePassword("Password1!")
        val result = viewModel.formState.value
        assertNull(result?.passwordError)
        assertFalse(result?.isValid?:true)
    }

    // confirm password tests
    @Test
    fun `confirmPassword diferente muestra error`() {
        viewModel.updatePassword("Password1!")
        viewModel.updateConfirmPassword("Password2!")

        val result = viewModel.formState.value
        assertEquals("Las contraseñas no coinciden", result?.currentPasswordError)
        assertFalse(result?.isValid ?: true)
    }
    @Test
    fun `confirmPassword igual no muestra error`() {
        viewModel.updatePassword("Password1!")
        viewModel.updateConfirmPassword("Password1!")
        val result = viewModel.formState.value
        assertNull(result?.currentPasswordError)
        assertFalse(result?.isValid ?: true)
    }

    // mixed tests
    @Test
    fun `formulario completo válido`() {
        viewModel.updateUsername("usuario.valido")
        viewModel.updateEmail("correo@email.com")
        viewModel.updatePassword("Password1!")
        val result = viewModel.formState.value
        assertNull(result?.usernameError)
        assertNull(result?.emailError)
        assertNull(result?.passwordError)
        assertTrue(result?.isValid?:false)
    }
    @Test
    fun `formulario con campos inválidos`() {
        viewModel.updateUsername("user")
        viewModel.updateEmail("correo@")
        viewModel.updatePassword("123")
        val result = viewModel.formState.value
        assertEquals("Nombre de usuario no válido", result?.usernameError)
        assertEquals("Introduce el correo electrónico en el formato someone@example.com", result?.emailError)
        assertNotNull(result?.passwordError)
        assertFalse(result?.isValid ?: true)
    }
}