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

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    // [START get_token]
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);

            /**
             * Initially this call goes out to the network to retrieve the token,
             * subsequent calls are local.
             */
            String token = instanceID.getToken(getString(R.string.sender_id), "GCM", null);

            /**
             * You should store a boolean that the generated token has been sent to your server.
             * If the boolean is false you should send the token to your server, otherwise your
             * server should have already received the token.
             */
            // [START_EXCLUDE]
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(this);
            boolean sentToken = sharedPreferences
                    .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (!sentToken) {
                // Send token to server.
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true);
                editor.commit();
            }

            /**
             * Use your generated token to send a GCM message to your application by
             * running the following command in your terminal from the root of the
             * GCMQuickstart.
             *
             * Windows
             * .\gradlew.bat run -Pargs="<message>,<token>"
             *
             * Linux/Mac
             * ./gradlew run -Pargs="<message>,<token>"
             */
            Log.d(TAG, "Token: " + token);
            // [END_EXCLUDE]
        } catch (IOException e) {
            Log.e(TAG, "Unable to get InstanceID token " + e);
            Log.e(TAG, getString(R.string.token_error_message));
            e.printStackTrace();
        }
        // [START_EXCLUDE]
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        // [START_EXCLUDE]
    }
    // [END get_token]

}
