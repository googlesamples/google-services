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
class AppDelegate: UIResponder, UIApplicationDelegate, GMPInstanceIDDelegate {

  var window: UIWindow?
  var gcmSenderID: String?
  var registrationOptions = [String: AnyObject]()

  let notificationKey = "onRegistrationCompleted"

  // [START register_for_remote_notifications]
  func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions:
      [NSObject: AnyObject]?) -> Bool {
    GGLContext.sharedInstance().configure()
    gcmSenderID = GGLContext.sharedInstance().configuration.gcmSenderID
    var types: UIUserNotificationType = UIUserNotificationType.Badge |
        UIUserNotificationType.Alert |
        UIUserNotificationType.Sound
    var settings: UIUserNotificationSettings =
        UIUserNotificationSettings( forTypes: types, categories: nil )
    application.registerUserNotificationSettings( settings )
    application.registerForRemoteNotifications()
  // [END register_for_remote_notifications]
  // [START start_gcm_service]
    GCMService.sharedInstance().startWithConfig(GCMConfig.defaultConfig())
  // [END start_gcm_service]
    return true
  }

  // [START connect_gcm_service]
  func applicationDidBecomeActive( application: UIApplication) {
    GCMService.sharedInstance().connectWithHandler({
        (NSError error) -> Void in
      if error != nil {
        println("Could not connect to GCM: \(error.localizedDescription)")
      } else {
        println("Connected to GCM")
      }
    })
  }
  // [END connect_gcm_service]

  // [START disconnect_gcm_service]
  func applicationDidEnterBackground(application: UIApplication) {
    GCMService.sharedInstance().disconnect()
  }
  // [END disconnect_gcm_service]

  // [START receive_apns_token]
  func application( application: UIApplication!, didRegisterForRemoteNotificationsWithDeviceToken
      deviceToken: NSData! ) {
  // [END receive_apns_token]
        // [START get_gcm_reg_token]
        GMPInstanceID.sharedInstance().startWithConfig(GMPInstanceIDConfig.defaultConfig())
        registrationOptions = [kGMPInstanceIDRegisterAPNSOption:deviceToken!,
          kGMPInstanceIDAPNSServerTypeSandboxOption:true]
        GMPInstanceID.sharedInstance().tokenWithAudience(gcmSenderID, scope: kGMPInstanceIDScopeGCM,
            options: registrationOptions, handler: registrationHandler)
        // [END get_gcm_reg_token]
  }

  // [START receive_apns_token_error]
  func application( application: UIApplication!, didFailToRegisterForRemoteNotificationsWithError
      error: NSError! ) {
    println("Registration for remote notification failed with error: \(error.localizedDescription)")
  // [END receive_apns_token_error]
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

  func registrationHandler(registrationToken: String!, error: NSError!) {
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
  }

  func onTokenRefresh(updateID: Bool) {
    println("The GCM registration token has been invalidated.")
    GMPInstanceID.sharedInstance().tokenWithAudience(gcmSenderID, scope: kGMPInstanceIDScopeGCM,
      options: registrationOptions, handler: registrationHandler)
  }

}
