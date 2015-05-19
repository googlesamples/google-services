/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.appinvite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Activity for displaying information about a receive App Invite invitation.  This activity
 * displays as a Dialog over the MainActivity and does not cover the full screen.
 */
public class DeepLinkActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = DeepLinkActivity.class.getSimpleName();

    // Client to connect to the AppInvite API
    private GoogleApiClient mGoogleApiClient;

    // Invitation intent received while GoogleApiClient was not connected, to be reported
    // on connection
    private Intent mCachedInvitationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deep_link_activity);

        // Button click listener
        findViewById(R.id.button_ok).setOnClickListener(this);

        // Note: for simplicity, this sample uses the 'enableAutoManage' feature of
        // GoogleApiClient.  This sample does not handle all possible error cases that
        // can arise when using enableAutoManage, so consult the documentation before
        // using enableAutoManage in a more complicated application
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .addApi(AppInvite.API)
                .build();
    }

    // [START deep_link_on_start]
    @Override
    protected void onStart() {
        super.onStart();

        // If app is already installed app and launched with deep link that matches
        // DeepLinkActivity filter, then the referral info will be in the intent
        Intent intent = getIntent();
        processReferralIntent(intent);
    }
    // [END deep_link_on_start]

    // [START process_referral_intent]
    private void processReferralIntent(Intent intent) {
        if (!AppInviteReferral.hasReferral(intent)) {
            Log.e(TAG, "Error: DeepLinkActivity Intent does not contain App Invite");
            return;
        }

        // Extract referral information from the intent
        String invitationId = AppInviteReferral.getInvitationId(intent);
        String deepLink = AppInviteReferral.getDeepLink(intent);

        // Display referral information
        // [START_EXCLUDE]
        Log.d(TAG, "Found Referral: " + invitationId + ":" + deepLink);
        ((TextView) findViewById(R.id.deep_link_text))
                .setText(getString(R.string.deep_link_fmt, deepLink));
        ((TextView) findViewById(R.id.invitation_id_text))
                .setText(getString(R.string.invitation_id_fmt, invitationId));
        // [END_EXCLUDE]

        if (mGoogleApiClient.isConnected()) {
            // Notify the API of the install success and invitation conversion
            updateInvitationStatus(intent);
        } else {
            // Cache the invitation ID so that we can call the AppInvite API after
            // the GoogleAPIClient connects
            Log.w(TAG, "Warning: GoogleAPIClient not connected, can't update invitation.");
            mCachedInvitationIntent = intent;
        }
    }
    // [END process_referral_intent]

    /** Update the install and conversion status of an invite intent **/
    // [START update_invitation_status]
    private void updateInvitationStatus(Intent intent) {
        String invitationId = AppInviteReferral.getInvitationId(intent);

        // Note: these  calls return PendingResult(s), so one could also wait to see
        // if this succeeds instead of using fire-and-forget, as is shown here
        if (AppInviteReferral.isOpenedFromPlayStore(intent)) {
            AppInvite.AppInviteApi.updateInvitationOnInstall(mGoogleApiClient, invitationId);
        }

        // If your invitation contains deep link information such as a coupon code, you may
        // want to wait to call `convertInvitation` until the time when the user actually
        // uses the deep link data, rather than immediately upon receipt
        AppInvite.AppInviteApi.convertInvitation(mGoogleApiClient, invitationId);
    }
    // [END update_invitation_status]

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "googleApiClient:onConnected");

        // We got a referral invitation ID before the GoogleApiClient was connected,
        // so send it now
        if (mCachedInvitationIntent != null) {
            updateInvitationStatus(mCachedInvitationIntent);
            mCachedInvitationIntent = null;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "googleApiClient:onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "googleApiClient:onConnectionFailed:" + connectionResult.getErrorCode());
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            Log.w(TAG, "onConnectionFailed because an API was unavailable");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ok:
                finish();
                break;
        }
    }
}
