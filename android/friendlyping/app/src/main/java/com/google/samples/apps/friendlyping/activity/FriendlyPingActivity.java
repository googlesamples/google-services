/*
 * Copyright Google Inc.
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

package com.google.samples.apps.friendlyping.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.samples.apps.friendlyping.R;
import com.google.samples.apps.friendlyping.fragment.FriendlyPingFragment;
import com.google.samples.apps.friendlyping.fragment.SignInFragment;

public class FriendlyPingActivity extends AppCompatActivity {

    private static final String TAG = "FriendlyPingActivity";

    /* RequestCode for resolutions involving sign-in */
    private static final int REQUEST_CODE_SIGN_IN = 9001;
    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    /* Client for accessing Google APIs */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    private View mProgressView;
    private View mContentView;

    private SignInFragment mSignInFragment;
    private FriendlyPingFragment mFriendlyPingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendly_ping);
        mProgressView = findViewById(R.id.progress);
        mContentView = findViewById(R.id.fragment);
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mConnectionFailedListener)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mIsResolving);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further errors.
            if (resultCode != Activity.RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friendly_ping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sign_out) {
            if (mGoogleApiClient == null) {
                Log.w(TAG, "GoogleApiClient is null. Make sure to set it before accessing it.");
            } else {
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, getSignInFragment()).commit();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private SignInFragment getSignInFragment() {
        if (null == mSignInFragment) {
            mSignInFragment = new SignInFragment();
            mSignInFragment.setGoogleApiClient(mGoogleApiClient);
        }
        return mSignInFragment;
    }

    private FriendlyPingFragment getFriendlyPingFragment() {
        if (null == mFriendlyPingFragment) {
            mFriendlyPingFragment = new FriendlyPingFragment();
        }
        return mFriendlyPingFragment;
    }

    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks
            = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            // onConnected indicates that an account was selected on the device, that the selected
            // account has granted any requested permissions to our app and that we were able to
            // establish a service connection to Google Play services.
            Log.d(TAG, "onConnected:" + bundle);
            // FIXME: 4/28/15 Show the signed-in UI
            mProgressView.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, getFriendlyPingFragment()).commit();
        }

        @Override
        public void onConnectionSuspended(int i) {
            // The connection to Google Play services was lost. The GoogleApiClient will
            // automatically attempt to re-connect. Any UI elements that depend on connection
            // to Google APIs should be hidden or disabled until onConnected is called again.
            Log.w(TAG, "onConnectionSuspended:" + i);
        }
    };

    private GoogleApiClient.OnConnectionFailedListener mConnectionFailedListener
            = new GoogleApiClient.OnConnectionFailedListener() {

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            // Could not connect to Google Play Services.  The user needs to select an account,
            // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
            // ConnectionResult to see possible error codes.
            Log.d(TAG, "onConnectionFailed: " + connectionResult);

            if (!mIsResolving && mShouldResolve) {
                if (connectionResult.hasResolution()) {
                    try {
                        connectionResult.startResolutionForResult(FriendlyPingActivity.this,
                                REQUEST_CODE_SIGN_IN);
                        mIsResolving = true;
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Could not resolve ConnectionResult.", e);
                        mIsResolving = false;
                        mGoogleApiClient.connect();
                    }
                } else {
                    showErrorDialog(connectionResult);
                }
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, getSignInFragment()).commit();

                mProgressView.setVisibility(View.GONE);
                mContentView.setVisibility(View.VISIBLE);

                mShouldResolve = true;
            }
        }

        private void showErrorDialog(ConnectionResult connectionResult) {
            int errorCode = connectionResult.getErrorCode();
            if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
                // Show the default Google Play services error dialog which may still start an
                // intent on our behalf if the user can resolve the issue.
                GooglePlayServicesUtil.getErrorDialog(errorCode,
                        FriendlyPingActivity.this, REQUEST_CODE_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mShouldResolve = false;
                                // FIXME: 4/27/15 handle failing
                            }
                        }).show();
            } else {
                // No default Google Play Services error, display a Toast
                Toast.makeText(FriendlyPingActivity.this,
                        getString(R.string.play_services_error_fmt, errorCode), Toast.LENGTH_SHORT)
                        .show();

                mShouldResolve = false;
                // FIXME: 4/27/15 handle failing
            }
        }
    };

}
