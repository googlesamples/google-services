//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
//  AppDelegate.swift
//  appinvites
import UIKit


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
  var window: UIWindow?

  func application(application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
      // Initialize sign-in
      GIDSignIn.sharedInstance().clientID = "YOUR_CLIENT_ID.apps.googleusercontent.com"
      GIDSignIn.sharedInstance().scopes = ["https://www.googleapis.com/auth/plus.login"]
      GPPInvite.applicationDidFinishLaunching()
      return true
  }

  func application(application: UIApplication,
    openURL url: NSURL, sourceApplication: String?, annotation: AnyObject?) -> Bool {
      return GIDSignIn.sharedInstance().handleURL(url, sourceApplication: sourceApplication, annotation: annotation)
  }
}

