package com.google.samples.quickstart.canonical

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
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
        // Your google account must have logged in before test.
        // Otherwise the main page will be changed to login page,
        // and all the test will fail.
        // You only need to login the first time you open the app after
        // installing it.
    }


    @Test
    fun searchDestination() {
        onView(ViewMatchers.withId(R.id.bottom_navigation_item_map))
            .perform(ViewActions.click())
        Thread.sleep(3000)
        onView(ViewMatchers.withId(R.id.autocomplete_fragment))
            .perform(ViewActions.click())
        val new = device.findObject(UiSelector().text("Search"))
        new.text = "Central Park"
        Thread.sleep(2000)
        device.click(300,800)
        Thread.sleep(2000)
    }
}