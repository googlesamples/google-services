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

package com.google.samples.apps.friendlyping.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.samples.apps.friendlyping.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";
    private GoogleApiClient mGoogleApiClient;

    public SignInFragment() {
        /* no-op */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();
        if (null != activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(
                    getResources().getColor(R.color.sign_in_status));
        }
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void setGoogleApiClient(GoogleApiClient client) {
        mGoogleApiClient = client;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient == null) {
                    Log.w(TAG, "GoogleApiClient is not set. Make sure to set it before trying "
                            + "to connect to it.");
                } else {
                    mGoogleApiClient.connect();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
