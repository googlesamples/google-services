package com.google.samples.quickstart.bannerexample;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * An Espresso test for the MainActivity.
 */
@LargeTest
public class MainActivityEspressoTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityEspressoTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testContentVisibleOnActivityStart() {
        onView(withId(R.id.content)).check(matches(isDisplayed()));
    }

    public void testAdViewVisibleOnActivityStart() {
        onView(withId(R.id.adView)).check(matches(isDisplayed()));
    }

}
