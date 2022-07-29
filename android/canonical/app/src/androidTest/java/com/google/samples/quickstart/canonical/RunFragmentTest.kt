package com.google.samples.quickstart.canonical

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RunFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    private lateinit var device : UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.sign_in_button))
            .perform(click())
        val googleSignInDialog = device.findObject(UiSelector().text("example@gmail.com"))
        googleSignInDialog.clickAndWaitForNewWindow()
        // Make sure that:
        // 1. Your google account must have signed out before test.
        // 2. You should have at lest one google account for your device,
        // which means, when you click sign in button, you have at least
        // one account to choose
    }

    @Test
    fun startClick() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))
        Thread.sleep(2000)
    }

    @Test
    fun pauseClick() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))
    }

    @Test
    fun startAndPauseClick() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:03")))
    }

    @Test
    fun resetClickWhenWorking() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.reset_btn))
            .perform(click())
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:00")))
    }

    @Test
    fun resetClickWhenPausing() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))

        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))

        onView(withId(R.id.reset_btn))
            .perform(click())
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:00")))
    }

    @Test
    fun submitClickWhenWorking() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))

        onView(withId(R.id.submit_btn))
            .perform(click())
        onView(withText("Submission Confirm"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withText("Cancel"))
            .inRoot(isDialog())
            .perform(click())
    }

    @Test
    fun submitClickWhenPausing() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.submit_btn))
            .perform(click())
        onView(withText("Submission Confirm"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
        onView(withText("Cancel"))
            .inRoot(isDialog())
            .perform(click())
    }

    @Test
    fun submitClickBeforeWorking() {
        onView(withId(R.id.submit_btn))
            .perform(click())
        onView(withText(R.string.submit_illegal))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

    @Test
    fun submitCancel() {
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.submit_btn))
            .perform(click())
        onView(withText("Cancel"))
            .inRoot(isDialog())
            .perform(click())

        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))
    }

    @Test
    fun switchFragmentWhenWorking() {
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)

        // Fragment transition needs time
        onView(withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        Thread.sleep(500)
        onView(withId(R.id.bottom_navigation_item_run))
            .perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:04")))
    }

    @Test
    fun switchFragmentWhenPausing() {
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.start_pause_btn))
            .perform(click())

        // Fragment transition needs time
        onView(withId(R.id.bottom_navigation_item_profile))
            .perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.bottom_navigation_item_run))
            .perform(click())

        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))
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