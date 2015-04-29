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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.samples.apps.friendlyping.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendlyPingFragment extends Fragment {

    private static final String TAG = "FriendlyPingFragment";
    private ListView mListView;

    public FriendlyPingFragment() {
        /* no-op */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friendly_ping, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.ping_list);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar_ping));
                //noinspection ConstantConditions
                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                activity.getWindow().setStatusBarColor(
                        getResources().getColor(R.color.primary_dark));
            }
        }

        // TODO: 4/23/15 set adapter to display received pings
        final String deviceId = getString(R.string.test_device_id);
        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice(deviceId).build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_friendly_ping, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
}
