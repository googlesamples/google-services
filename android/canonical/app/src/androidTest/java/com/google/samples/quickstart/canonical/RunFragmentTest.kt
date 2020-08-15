package com.google.samples.quickstart.canonical

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RunFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Before
    fun makeSureYourAccountHasLoggedIn() {
        // Your google account must have logged in before test.
        // Otherwise the main page will be changed to login page,
        // and all the test will fail.
        // You only need to login the first time you open the app after
        // installing it.
    }

    @Test
    fun startClickTest() {
//        launchFragmentInContainer<RunFragment>()
        onView(withId(R.id.start_pause_btn))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.running_chronometer))
            .check(matches(withText("00:02")))
    }

    @Test
    fun pauseClickTest() {
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
    fun startAndPauseClickTest() {
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
    fun resetClickWhenWorkingTest() {
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
    fun resetClickWhenPausingTest() {
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
    fun submitClickWhenWorkingTest() {
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
    }

    @Test
    fun submitClickWhenPausingTest() {
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
    }

    @Test
    fun submitClickBeforeWorkingTest() {
        onView(withId(R.id.submit_btn))
            .perform(click())
        onView(withText(R.string.submit_illegal))
            .inRoot(withDecorView(not(activityRule.activity.window.decorView)))
            .check(matches(isDisplayed()))
    }

}