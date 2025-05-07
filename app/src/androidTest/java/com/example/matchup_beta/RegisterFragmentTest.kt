package com.example.matchup_beta

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @Test
    fun testFragmentIsVisible() {
        val scenario = ActivityScenario.launch(TestActivity::class.java)

        scenario.onActivity { activity ->
            val fragment = RegisterFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commitNow()
        }

        onView(withId(R.id.editTextUsername)).check(matches(isDisplayed()))
    }

    @Test
    fun testNameIsBlank_showsError() {
        launchRegisterFragment()

        onView(withId(R.id.editTextUsername)).perform(typeText("usuario123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(clearText(), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("andres@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("Password1!"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("Password1!"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.editTextName))
            .check(matches(hasErrorText("Campo requerido")))
    }

    @Test
    fun testEmailIsBlank_showsError() {
        launchRegisterFragment()

        onView(withId(R.id.editTextUsername)).perform(typeText("usuario123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText("Andres"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(clearText(), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("Password1!"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("Password1!"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.editTextEmail))
            .check(matches(hasErrorText("Campo requerido")))
    }

    @Test
    fun testPasswordMismatch_showsError() {
        launchRegisterFragment()

        onView(withId(R.id.editTextUsername)).perform(typeText("usuario123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText("Andres"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("andres@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("Password1!"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("Different1!"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        onView(withId(R.id.editTextConfirmPassword))
            .check(matches(hasErrorText("Las contraseñas no coinciden")))
    }

    private fun launchRegisterFragment() {
        ActivityScenario.launch(TestActivity::class.java).onActivity { activity ->
            val fragment = RegisterFragment()
            activity.supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, fragment)
                .commitNow()
        }
    }
}