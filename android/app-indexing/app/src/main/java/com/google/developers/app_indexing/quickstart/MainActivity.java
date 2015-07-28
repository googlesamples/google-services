/*
 * Copyright 2014 Google Inc. All Rights Reserved.
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

package com.google.developers.app_indexing.quickstart;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    private GoogleApiClient mClient;
    private String articleId;

    // [START handle_intent]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // [START_EXCLUDE]
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // [END_EXCLUDE]
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.APP_INDEX_API).build();
        onNewIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            articleId = data.substring(data.lastIndexOf("/") + 1);
            TextView deepLinkText = (TextView)findViewById(R.id.deep_link);
            deepLinkText.setText(data);
        }
    }
    // [END handle_intent]

    // [START app_indexing_view]
    @Override
    public void onStart(){
        super.onStart();

        if (articleId != null) {
            // Connect your client
            mClient.connect();

            // Define a title for your current page, shown in autocompletion UI
            final String TITLE = "Sample Article";
            final Uri APP_URI = Uri.parse("android-app://com.google.developers.app_indexing.quickstart/http/www.example.com/articles/" + articleId);
            final Uri WEB_URL = Uri.parse("http://www.example.com/articles/" + articleId);

            // Call the App Indexing API view method
            PendingResult<Status> result = AppIndex.AppIndexApi.view(mClient, this,
                    APP_URI, TITLE, WEB_URL, null);

            result.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Log.d(TAG, "App Indexing API: Recorded page view successfully.");
                    } else {
                        Log.e(TAG, "App Indexing API: There was an error recording the page view."
                                + status.toString());
                    }
                }
            });
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        if (articleId != null) {
            final Uri APP_URI = Uri.parse("android-app://com.google.developers.app-indexing.quickstart/http/www.example.com/articles/" + articleId);
            PendingResult<Status> result = AppIndex.AppIndexApi.viewEnd(mClient, this, APP_URI);

            result.setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Log.d(TAG, "App Indexing API: Recorded recipe view end successfully.");
                    } else {
                        Log.e(TAG, "App Indexing API: There was an error recording the recipe view."
                                + status.toString());
                    }
                }
            });

            mClient.disconnect();
        }
    }
    // [END app_indexing_view]

}
