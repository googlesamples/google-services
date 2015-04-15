/**
 * Copyright 2015 Google Inc. All Rights Reserved.
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

package gcm.play.android.samples.com.gcmquickstart;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if server rotates InstanceID token. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     *
     * @param updateID If true a new InstanceID is issued. If false only the token
     *                    has been updated.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh(boolean updateID) {
        /**
         * Set boolean that current token has not been sent to server.
         */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
        editor.commit();

        /**
         * This is where you would update the application server with new token.
         */
        try {
            String token = InstanceID.getInstance(this).getToken(getString(R.string.sender_id),
                    "GCM", null);
            Log.d(TAG, "Refreshed token: " + token);

            // Send new token to server if another call has not already done so.
            if (!sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false)) {
                // Send token to your server.

                // Update boolean to show that token has been sent to server.
                editor.putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true);
                editor.commit();
            }
        } catch (IOException e) {
            Log.d(TAG, "Unable to retrieve new token. " + e.getMessage());
            e.printStackTrace();
        }
    }
    // [END refresh_token]
}
