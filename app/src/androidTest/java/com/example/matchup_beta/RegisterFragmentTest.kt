package com.example.matchup_beta


import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.Root
import android.view.WindowManager
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @Test
    fun testNameIsBlank_showsToast() {
        launchFragmentInContainer<RegisterFragment>(themeResId = R.style.Theme_MatchUpBETA)

        onView(withId(R.id.editTextUsername)).perform(typeText("usuario123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(clearText())
        onView(withId(R.id.registerButton)).perform(click())

        onView(withText("Por favor, completa todos los campos"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun testEmailIsBlank_showsToast() {
        launchFragmentInContainer<RegisterFragment>(themeResId = R.style.Theme_MatchUpBETA)

        onView(withId(R.id.editTextUsername)).perform(typeText("usuario123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText("Andres"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(clearText())
        onView(withId(R.id.registerButton)).perform(click())

        onView(withText("Por favor, completa todos los campos"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun testPasswordMismatch_showsToast() {
        launchFragmentInContainer<RegisterFragment>(themeResId = R.style.Theme_MatchUpBETA)

        onView(withId(R.id.editTextUsername)).perform(typeText("usuario123"), closeSoftKeyboard())
        onView(withId(R.id.editTextName)).perform(typeText("Andres"), closeSoftKeyboard())
        onView(withId(R.id.editTextEmail)).perform(typeText("andres@example.com"), closeSoftKeyboard())
        onView(withId(R.id.editTextPassword)).perform(typeText("Password1!"), closeSoftKeyboard())
        onView(withId(R.id.editTextConfirmPassword)).perform(typeText("Password2!"), closeSoftKeyboard())

        onView(withId(R.id.registerButton)).perform(click())

        onView(withText("Las contraseñas no coinciden"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }
}

class ToastMatcher : TypeSafeMatcher<Root>() {
    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        val type = root.windowLayoutParams?.get()?.type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken = root.decorView.windowToken
            val appToken = root.decorView.applicationWindowToken
            return windowToken === appToken
        }
        return false
    }
}
