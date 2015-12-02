//
//  Copyright (c) Google Inc.
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
import UIKit

@UIApplicationMain
// [START appdelegate_interfaces]
class AppDelegate: UIResponder, UIApplicationDelegate, GIDSignInDelegate {
// [END appdelegate_interfaces]
  var window: UIWindow?

  // [START didfinishlaunching]
  func application(application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
      // Initialize sign-in
      var configureError: NSError?
      GGLContext.sharedInstance().configureWithError(&configureError)
      assert(configureError == nil, "Error configuring Google services: \(configureError)")

      GIDSignIn.sharedInstance().delegate = self

      return true
  }
  // [END didfinishlaunching]

  // [START openurl]
  func application(application: UIApplication,
    openURL url: NSURL, sourceApplication: String?, annotation: AnyObject) -> Bool {
      return GIDSignIn.sharedInstance().handleURL(url,
          sourceApplication: sourceApplication,
          annotation: annotation)
  }
  // [END openurl]

  @available(iOS 9.0, *)
  func application(app: UIApplication, openURL url: NSURL, options: [String : AnyObject]) -> Bool {
    return GIDSignIn.sharedInstance().handleURL(url,
      sourceApplication: options[UIApplicationOpenURLOptionsSourceApplicationKey] as! String?,
      annotation: options[UIApplicationOpenURLOptionsAnnotationKey])
  }

  // [START signin_handler]
  func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!,
    withError error: NSError!) {
      if (error == nil) {
        // Perform any operations on signed in user here.
        let userId = user.userID                  // For client-side use only!
        let idToken = user.authentication.idToken // Safe to send to the server
        let name = user.profile.name
        let email = user.profile.email
        // [START_EXCLUDE]
        NSNotificationCenter.defaultCenter().postNotificationName(
            "ToggleAuthUINotification",
            object: nil,
            userInfo: ["statusText": "Signed in user:\n\(name)"])
        // [END_EXCLUDE]
      } else {
        print("\(error.localizedDescription)")
        // [START_EXCLUDE silent]
        NSNotificationCenter.defaultCenter().postNotificationName(
          "ToggleAuthUINotification", object: nil, userInfo: nil)
        // [END_EXCLUDE]
      }
  }
  // [END signin_handler]

  // [START disconnect_handler]
  func signIn(signIn: GIDSignIn!, didDisconnectWithUser user:GIDGoogleUser!,
    withError error: NSError!) {
      // Perform any operations when the user disconnects from app here.
      // [START_EXCLUDE]
      NSNotificationCenter.defaultCenter().postNotificationName(
          "ToggleAuthUINotification",
          object: nil,
          userInfo: ["statusText": "User has disconnected."])
      // [END_EXCLUDE]
  }
  // [END disconnect_handler]

}
