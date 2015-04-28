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

  let notificationKey = "onRegistrationCompleted"
  // TODO(silvano): move to info.plist
  let senderId = "177545629583"

  func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions:
      [NSObject: AnyObject]?) -> Bool {
    var types: UIUserNotificationType = UIUserNotificationType.Badge |
        UIUserNotificationType.Alert |
        UIUserNotificationType.Sound
    var settings: UIUserNotificationSettings =
        UIUserNotificationSettings( forTypes: types, categories: nil )
    application.registerUserNotificationSettings( settings )
    application.registerForRemoteNotifications()
    return true
  }

  func application( application: UIApplication!, didRegisterForRemoteNotificationsWithDeviceToken
      deviceToken: NSData! ) {
        // [START get_gcm_reg_token]
        GMPInstanceID.sharedInstance().startWithConfig(GMPInstanceIDConfig.defaultConfig())
        var options = [kGMPInstanceIDRegisterAPNSOption:deviceToken!,
          kGMPInstanceIDAPNSServerTypeSandboxOption:true]
        GMPInstanceID.sharedInstance().tokenWithAudience(senderId, scope: kGMPInstanceIDScopeGCM,
            options: options, handler: {
              (registrationToken: String!, error: NSError!) -> Void in
                if (registrationToken != nil) {
                  println("Registration Token: \(registrationToken)")
                  let userInfo = ["registrationToken": registrationToken]
                  NSNotificationCenter.defaultCenter().postNotificationName(
                      self.notificationKey, object: nil, userInfo: userInfo)
                } else {
                  println("Registration to GCM failed with error: \(error.localizedDescription)")
                  let userInfo = ["error": error.localizedDescription]
                  NSNotificationCenter.defaultCenter().postNotificationName(
                      self.notificationKey, object: nil, userInfo: userInfo)
              }
        })
        // [END get_gcm_reg_token]
  }

  func application( application: UIApplication!, didFailToRegisterForRemoteNotificationsWithError
      error: NSError! ) {
    println("Registration for remote notification failed with error: \(error.localizedDescription)")
    let userInfo = ["error": error.localizedDescription]
    NSNotificationCenter.defaultCenter().postNotificationName(
        notificationKey, object: nil, userInfo: userInfo)
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
