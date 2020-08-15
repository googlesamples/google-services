package com.google.samples.quickstart.canonical

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseInteractionTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    lateinit var device : UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(ViewMatchers.withId(R.id.sign_in_button))
            .perform(click())
        val googleSignInDialog = device.findObject(UiSelector().text("yl4324@columbia.edu"))
        googleSignInDialog.clickAndWaitForNewWindow()
        // Make sure that:
        // 1. Your google account must have signed out before test.
        // 2. You should have at lest one google account for your device,
        // which means, when you click sign in button, you have at least
        // one account to choose
    }

    @Test
    fun submitRecord() {
        onView(ViewMatchers.withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(5000)

        onView(ViewMatchers.withId(R.id.submit_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(ViewMatchers.withText("Submission Confirm"))
            .inRoot(isDialog())
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("Confirm"))
            .inRoot(isDialog())
            .perform(click())
        Thread.sleep(1000)

        onView(ViewMatchers.withId(R.id.bottom_navigation_item_profile))
            .perform(click())

        onView(ViewMatchers.withId(R.id.run_history_list_view))
            .check(matches(ViewMatchers.isDisplayed()))
        onData(Matchers.anything())
            .inAdapterView(ViewMatchers.withId(R.id.run_history_list_view))
            .atPosition(0)
            .onChildView(ViewMatchers.withId(R.id.single_run_time))
            .check(matches(ViewMatchers.withText("00:00:05")))
            .perform(click())
    }

    @After
    fun logoutUser() {
        onView(ViewMatchers.withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        onView(ViewMatchers.withId(R.id.logout_button))
            .perform(click())
        Thread.sleep(1000)
    }
}