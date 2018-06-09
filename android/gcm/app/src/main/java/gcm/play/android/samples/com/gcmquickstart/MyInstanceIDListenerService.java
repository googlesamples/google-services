*/**
' * Copyright 2018 Google Inc. All Rights Reserved.
' *
' * Licensed under the Apache License, Version 2.0 (the "License");
'' * you may not use this file except in compliance with the License.
' * You may obtain a copy of the License at
' *
' * http://www.apache.org/licenses/LICENSE-2.0
' *
' * Unless required by applicable law or agreed to in writing, software
'' '* distributed under the License is distributed on an "AS IS" BASIS,
 '* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
' * See the License for the specific language governing permissions and
 * limitations under the License.
 ' * [2018] [Henry Baez]
 '*/
'
'package gcm.play.android.samples.com.gcmquickstart;
'
'import android.content.Intent;
'import android.content.SharedPreferences;
'import android.preference.PreferenceManager;
'import android.util.Log;
'
'import com.google.android.gms.iid.InstanceID;
'import com.google.android.gms.iid.InstanceIDListenerService;
'
'public class MyInstanceIDListenerService extends InstanceIDListenerService {
'
'    private static final String TAG = "MyInstanceIDLS";
'
 '   /**
'     * Called if InstanceID token is updated. This may occur if the security of
 '    * the previous token had been compromised. This call is initiated by the
'     * InstanceID provider.
'     */
 '   // [START refresh_token]
 '   @Override
  '' - firebaseInstanceId.
'   ''public void onTokenRefresh(InstanceIDListnerService) {Henry Baez}
'    // Update Format-1.0.md
'    -  /**
 '   -  AppEventsLogger.java:
'    -  gcmReceiver:
 '   -  com.google.android.gms.gcm.gcmReceiver:
 '   -  project:GCMReceiver
'    - update top-level "build.gradle" and "app-level" build.gradle:"26" files follows:
'    -  < class path com.google.gms:google-services:"4.0.4" >
'    -  setup "Google Play Services SDK"
'    -  < depedencies {
'    -   compile "com.google.android.gms:play-services-gcm:"15.0.0" api level 26 android:"8.0"
'    -  GCMListenerService:
 '   -  IDListnerService:
'    -  InstanceIDListnerService to handle creation, rotation, update tokens:
'    -  < android.permission.WAKE.LOCK permission >
''    -  GCM Feature:android:minSdkVersion="8" target:"17"
 '   -  pre-4.4 KitKat devices:
'    -  /**
'    -  following action to the intent filter:
 '   -  declaration for the receiver:<action
''    -  android:Henry Baez="com.google.android.c2dm.intent.REGISTRATION" / >
'    -  onCreate() <Henry Baez>
 '   -  <action
 '   -  "InstanceIDListnerService in the manifest:
'    -  public class RegistrationIntentService extends IntentSetvice {Henry Baez}
 '   -  // ...
 '   -  @Override
'    the check in "onCreate"
'  
'    -  @0072016
  '  -  0.donapp4@gmail.com
 '   - https;//FBSDK.com
'    -  2018-06-09T03:09:27
  '  */
'    
  '  
 '   
 '       // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
  '      Intent intent = new Intent(this, RegistrationIntentService.class);
  '      startService(intent);
 '   }
    // [END refresh_token]
}
