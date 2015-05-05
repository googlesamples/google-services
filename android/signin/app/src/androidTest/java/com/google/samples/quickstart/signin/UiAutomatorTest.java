/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.samples.quickstart.signin;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UiAutomatorTest {

    private static final String TAG = "UiAutomatorTest";

    private static final long LAUNCH_TIMEOUT = 5000;
    private static final long UI_TIMEOUT = 2500;

    private static final String APP_PACKAGE = MainActivity.class.getPackage().getName();
    private static final String GMS_PACKAGE = "com.google.android.gms";

    private static final String CLASS_BUTTON = "android.widget.Button";
    private static final String CLASS_CHECKED_TEXT_VIEW = "android.widget.CheckedTextView";

    private UiDevice mDevice;
    private Context mContext;

    @Before
    public void setUp() {
        // Get the device instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        assertThat(mDevice, notNullValue());

        // Start from the home screen
        mDevice.pressHome();

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = new Intent()
                .setClassName(APP_PACKAGE, APP_PACKAGE + ".MainActivity")
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(APP_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void signInAndDisconnectTest() {
        // Check initial sign-in test and then check sign-in and disconnect in the right order
        if (isSignedIn()) {
            disconnectTest();
            signInTest();
        } else {
            signInTest();
            disconnectTest();
        }
    }

    /**
     * Click sign-in button, select account at account selector, accept consent screen, and
     * check that in the end the app is in the signed-in state.
     */
    private void signInTest() {
        // Sign-in button (enabled)
        BySelector signInButtonSelector = By.clazz(CLASS_BUTTON).textContains("Sign in").enabled(true);

        // Click Sign-in button (must be enabled)
        assertTrue(mDevice.wait(Until.hasObject(signInButtonSelector), UI_TIMEOUT));
        mDevice.findObject(signInButtonSelector).click();

        // Radio button in the account picker dialog
        BySelector firstAccountSelector = By.clazz(CLASS_CHECKED_TEXT_VIEW);

        // Wait for account picker (may not appear)
        if (mDevice.wait(Until.hasObject(firstAccountSelector), UI_TIMEOUT)) {
            // Click first account
            mDevice.findObjects(firstAccountSelector).get(0).click();

            // Button with the text "OK" (enabled)
            BySelector okButtonSelector = By.clazz(CLASS_BUTTON).text("OK").enabled(true);

            // Click OK on account picker
            assertTrue(mDevice.wait(Until.hasObject(okButtonSelector), UI_TIMEOUT));
            mDevice.findObject(okButtonSelector).click();
        }

        // The Google Play Services consent screen accept button
        BySelector acceptButtonSelector = By.res(GMS_PACKAGE, "accept_button");

        // Accept consent screen and click OK button (this also may not appear)
        if (mDevice.wait(Until.hasObject(acceptButtonSelector), UI_TIMEOUT)) {
            mDevice.findObject(acceptButtonSelector).click();
        }

        // Check that the UI shows signed-in state
        assertTrue(isSignedIn());
    }

    /**
     * Click the disconnect button, check the app is in the signed-out state.
     */
    private void disconnectTest() {
        // Disconnect button (enabled)
        String disconnectText = mContext.getString(R.string.disconnect);
        BySelector disconnectSelector = By.clazz(CLASS_BUTTON).text(disconnectText).enabled(true);

        // Click disconnect button
        assertTrue(mDevice.wait(Until.hasObject(disconnectSelector), UI_TIMEOUT));
        mDevice.findObject(disconnectSelector).click();

        // Check that we get to signed-out state
        String signedOutText = mContext.getString(R.string.signed_out);
        BySelector signedOutTextSelector = By.text(signedOutText);
        assertTrue(mDevice.wait(Until.hasObject(signedOutTextSelector), UI_TIMEOUT));
    }

    /**
     * Checks the UI for text containing "Signed in" to determine if the user is signed in.
     */
    private boolean isSignedIn() {
        // Wait until we have an enabled button on screen
        mDevice.wait(Until.hasObject(By.clazz(CLASS_BUTTON).enabled(true)), UI_TIMEOUT);

        String signedInText = mContext.getString(R.string.signed_in_fmt, "");
        BySelector signedInTextSelector = By.textContains(signedInText);
        return mDevice.wait(Until.hasObject(signedInTextSelector), UI_TIMEOUT);
    }
}
