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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteReferral;

/**
 * Main Activity for sending App Invites and launching the DeepLinkActivity when an
 * App Invite is received.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;

    // Local Broadcast receiver for receiving invites
    private BroadcastReceiver mDeepLinkReceiver = null;

    // [START on_create]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // [START_EXCLUDE]
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Invite button click listener
        findViewById(R.id.invite_button).setOnClickListener(this);
        // [END_EXCLUDE]

        if (savedInstanceState == null) {
            // No savedInstanceState, so it is the first launch of this activity
            Intent intent = getIntent();
            if (AppInviteReferral.hasReferral(intent)) {
                // In this case the referral data is in the intent launching the MainActivity,
                // which means this user already had the app installed. We do not have to
                // register the Broadcast Receiver to listen for Play Store Install information
                launchDeepLinkActivity(intent);
            }
        }
    }
    // [END on_create]

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();
        registerDeepLinkReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterDeepLinkReceiver();
    }
    // [END on_start_on_stop]

    /**
     * User has clicked the 'Invite' button, launch the invitation UI with the proper
     * title, message, and deep link
     */
    // [START on_invite_clicked]
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    // [END on_invite_clicked]

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent and show message to the user
                // The ids array contains the unique invitation ids for each invitation sent
                // (one for each contact select by the user). You can use these for analytics
                // as the ID will be consistent on the sending and receiving devices.
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                showMessage(getString(R.string.sent_invitations_fmt, ids.length));
            } else {
                // Sending failed or it was canceled, show failure message to the user
                showMessage(getString(R.string.send_failed));
            }
        }
    }
    // [END on_activity_result]

    private void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.snackbar_layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_button:
                onInviteClicked();
                break;
        }
    }

    /**
     * There are two broadcast receivers in this application.  The first is ReferrerReceiver, it
     * is a global receiver declared in the manifest.  It receives broadcasts from the Play Store
     * and then broadcasts messages to the local broadcast receiver, which is registered here.
     * Since the broadcast is asynchronous, it can occur after the app has started, so register
     * for the notification immediately in onStart. The Play Store broadcast should be very soon
     * after the app is first opened, so this receiver should trigger soon after start
     */
    // [START register_unregister_launch]
    private void registerDeepLinkReceiver() {
        // Create local Broadcast receiver that starts DeepLinkActivity when a deep link
        // is found
        mDeepLinkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AppInviteReferral.hasReferral(intent)) {
                    launchDeepLinkActivity(intent);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(getString(R.string.action_deep_link));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mDeepLinkReceiver, intentFilter);
    }

    private void unregisterDeepLinkReceiver() {
        if (mDeepLinkReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mDeepLinkReceiver);
        }
    }

    /**
     * Launch DeepLinkActivity with an intent containing App Invite information
     */
    private void launchDeepLinkActivity(Intent intent) {
        Log.d(TAG, "launchDeepLinkActivity:" + intent);
        Intent newIntent = new Intent(intent).setClass(this, DeepLinkActivity.class);
        startActivity(newIntent);
    }
    // [END register_unregister_launch]
}

