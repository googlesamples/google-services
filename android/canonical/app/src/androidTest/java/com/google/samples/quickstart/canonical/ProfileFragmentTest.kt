package com.google.samples.quickstart.canonical

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.Matchers.anything
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    lateinit var device : UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.sign_in_button))
            .perform(click())
        val googleSignInDialog = device.findObject(UiSelector().text("exampleForProfileTest@gmail.com"))
        googleSignInDialog.clickAndWaitForNewWindow()
        Thread.sleep(1000)
        // Make sure that:
        // 1. Your google account must have signed out before test.
        // 2. You should have at lest one google account for your device,
        // which means, when you click sign in button, you have at least
        // one account to choose

        // Account: exampleForProfileTest@gmail.com
        // Display name: example user

        // Construct your test database:
        // User: exampleForProfileTest@gmail.com
        // Total running time: 00:05:39
        // Total energy consumed: 74
        // Latest running history:
        //      datetime :  2000-01-01 00:00:00
        //      time :      00:00:02

    }

    @Test
    fun checkUserInfoTest() {
        onView(withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        onView(withId(R.id.usr_img))
            .check(matches(isDisplayed()))
        onView(withId(R.id.usr_name))
            .check(matches(withText("example user")))
        onView(withId(R.id.usr_email))
            .check(matches(withText("exampleForProfileTest@gmail.com")))
    }

    @Test
    fun checkUserStatisticTest() {
        onView(withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        onView(withId(R.id.usr_run_time))
            .check(matches(withText("00:05:39")))
        onView(withId(R.id.usr_run_energy))
            .check(matches(withText("74")))
    }

    @Test
    fun checkUserRunHistoryTest() {
        onView(withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        onView(withId(R.id.run_history_list_view))
            .check(matches(isDisplayed()))
        onData(anything()).inAdapterView(withId(R.id.run_history_list_view))
            .atPosition(0)
            .onChildView(withId(R.id.single_run_datetime))
            .check(matches(withText("2000-01-01 00:00:00")))
            .perform(click())
        onData(anything()).inAdapterView(withId(R.id.run_history_list_view))
            .atPosition(0)
            .onChildView(withId(R.id.single_run_time))
            .check(matches(withText("00:00:02")))
            .perform(click())
    }


    @After
    fun logoutUser() {
        onView(withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        onView(withId(R.id.logout_button))
            .perform(click())
        Thread.sleep(1000)
    }
}