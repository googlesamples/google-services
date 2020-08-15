package com.google.samples.quickstart.canonical

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapsFragmentTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Rule @JvmField
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    lateinit var device : UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(ViewMatchers.withId(R.id.sign_in_button))
            .perform(ViewActions.click())
        val googleSignInDialog = device.findObject(UiSelector().text("sjtuly1996@gmail.com"))
        googleSignInDialog.clickAndWaitForNewWindow()
        // Make sure that:
        // 1. Your google account must have signed out before test.
        // 2. You should have at lest one google account for your device,
        // which means, when you click sign in button, you have at least
        // one account to choose
    }


    @Test
    fun searchDestination() {
        onView(ViewMatchers.withId(R.id.bottom_navigation_item_map))
            .perform(ViewActions.click())
        Thread.sleep(3000)
        onView(ViewMatchers.withId(R.id.autocomplete_fragment))
            .perform(ViewActions.click())
        val searchView = device.findObject(UiSelector().text("Search"))
        searchView.text = "Central Park"
        Thread.sleep(2000)
        device.click(300,800)
        Thread.sleep(2000)
    }

    @After
    fun logoutUser() {
        onView(ViewMatchers.withId(R.id.bottom_navigation_item_profile))
            .perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.logout_button))
            .perform(ViewActions.click())
        Thread.sleep(1000)
    }
}