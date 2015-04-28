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

// TODO(silvano): remember to change the bundle identifier

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

  var window: UIWindow?

  func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions:
      [NSObject: AnyObject]?) -> Bool {
    var types: UIUserNotificationType = UIUserNotificationType.Badge |
        UIUserNotificationType.Alert |
        UIUserNotificationType.Sound
    var settings: UIUserNotificationSettings =
        UIUserNotificationSettings( forTypes: types, categories: nil )
    application.registerUserNotificationSettings( settings )
    application.registerForRemoteNotifications()
    GMPInstanceID.sharedInstance().startWithConfig(GMPInstanceIDConfig.defaultConfig())
    return true
  }

  func application( application: UIApplication!, didRegisterForRemoteNotificationsWithDeviceToken
      deviceToken: NSData! ) {
    //TODO(silvano): check with Ian to refactor to notification or view controller observing
    if let rootViewController = window!.rootViewController as? ViewController {
      rootViewController.didReceiveAPNSToken(deviceToken)
    }
  }

  func application( application: UIApplication!, didFailToRegisterForRemoteNotificationsWithError
      error: NSError! ) {
    println("Registration for remote notification failed with error: \(error.localizedDescription)")
    let controller = UIAlertController(title: "Error registering for remote notification",
        message: error.localizedDescription, preferredStyle: .Alert)
        let dismissAction = UIAlertAction(title: "Dismiss", style: .Destructive, handler: nil)
    controller.addAction(dismissAction)
    window!.rootViewController?.presentViewController(controller, animated: true, completion: nil)
  }

  func application( application: UIApplication,
    didReceiveRemoteNotification userInfo: [NSObject : AnyObject]) {
      println("Notification received: \(userInfo)")
  }

  func application(application: UIApplication,
    didReceiveRemoteNotification userInfo: [NSObject : AnyObject],
    fetchCompletionHandler handler: (UIBackgroundFetchResult) -> Void) {
      println("Notification received: \(userInfo)")
      handler(UIBackgroundFetchResult.NoData)
  }

}
