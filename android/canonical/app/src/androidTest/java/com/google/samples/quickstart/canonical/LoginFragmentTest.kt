package com.google.samples.quickstart.canonical

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    lateinit var device : UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        Espresso.onView(ViewMatchers.withId(R.id.sign_in_button))
            .perform(ViewActions.click())
        val googleSignInDialog = device.findObject(UiSelector().text("example@gmail.com"))
        googleSignInDialog.clickAndWaitForNewWindow()
        // Make sure that:
        // 1. Your google account must have signed out before test.
        // 2. You should have at lest one google account for your device,
        // which means, when you click sign in button, you have at least
        // one account to choose
    }


    @Test
    fun logoutAndSignIn() {
        // Logout test
        Espresso.onView(ViewMatchers.withId(R.id.bottom_navigation_item_profile))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.logout_button))
            .perform(ViewActions.click())
        Thread.sleep(1000)

        // Sign in test
        Espresso.onView(ViewMatchers.withId(R.id.sign_in_button))
            .perform(ViewActions.click())
        val googleSignInDialog = device.findObject(UiSelector().text("example@gmail.com"))
        googleSignInDialog.clickAndWaitForNewWindow()
    }

    @Test
    fun signInFailureHandle() {
        // Logout
        Espresso.onView(ViewMatchers.withId(R.id.bottom_navigation_item_profile))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.logout_button))
            .perform(ViewActions.click())
        Thread.sleep(1000)

        // Sign in failure test
        Espresso.onView(ViewMatchers.withId(R.id.sign_in_button))
            .perform(ViewActions.click())
        device.pressBack()
        Espresso.onView(ViewMatchers.withText(R.string.login_failed))
            .inRoot(RootMatchers.withDecorView(IsNot.not(activityRule.activity.window.decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Sign in
        Espresso.onView(ViewMatchers.withId(R.id.sign_in_button))
            .perform(ViewActions.click())
        val googleSignInDialog = device.findObject(UiSelector().text("example@gmail.com"))
        googleSignInDialog.clickAndWaitForNewWindow()

    }


    @After
    fun logoutUser() {
        Espresso.onView(ViewMatchers.withId(R.id.bottom_navigation_item_profile))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.logout_button))
            .perform(ViewActions.click())
        Thread.sleep(1000)
    }
}