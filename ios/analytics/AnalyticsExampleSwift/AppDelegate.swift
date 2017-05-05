//
//  Copyright (c) 2015 Google Inc.
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
class AppDelegate: UIResponder, UIApplicationDelegate {

  var window: UIWindow?

  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {

    // [START tracker_swift]
    guard let gai = GAI.sharedInstance() else {
      assert(false, "Google Analytics not configured correctly")
    }
    gai.tracker(withTrackingId: "YOUR_TRACKING_ID")
    // Optional: automatically report uncaught exceptions.
    gai.trackUncaughtExceptions = true

    // Optional: set Logger to VERBOSE for debug information.
    // Remove before app release.
    gai.logger.logLevel = .verbose;
    // [END tracker_swift]

    // Set a white background so that patterns are showcased.
    window?.backgroundColor = UIColor.white

    return true
  }

}
