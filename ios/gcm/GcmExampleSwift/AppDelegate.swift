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
class AppDelegate: UIResponder, UIApplicationDelegate, GMSInstanceIDDelegate {

  var window: UIWindow?

  var connectedToGCM = false
  var subscribedToTopic = false
  var gcmSenderID: String?
  var registrationToken: String?
  var registrationOptions = [String: AnyObject]()

  let registrationKey = "onRegistrationCompleted"
  let messageKey = "onMessageReceived"
  let subscriptionTopic = "/topics/global"

  // [START register_for_remote_notifications]
  func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions:
      [NSObject: AnyObject]?) -> Bool {
    // [START_EXCLUDE]
    var configureError:NSError?
    GGLContext.sharedInstance().configureWithError(&configureError)
    if configureError != nil {
      println("Error configuring the Google context: \(configureError)")
    }
    gcmSenderID = GGLContext.sharedInstance().configuration.gcmSenderID
    // [END_EXCLUDE]
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

  func subscribeToTopic() {
    if(registrationToken != nil && connectedToGCM) {
      GCMPubSub.sharedInstance().subscribeWithToken(self.registrationToken, topic: subscriptionTopic,
        options: nil, handler: {(NSError error) -> Void in
          if (error != nil) {
            println("Subcription failed: \(error.localizedDescription)");
          } else {
            self.subscribedToTopic = true;
            NSLog("Subscribed to \(self.subscriptionTopic)");
          }
      })
    }
  }

  // [START connect_gcm_service]
  func applicationDidBecomeActive( application: UIApplication) {
    GCMService.sharedInstance().connectWithHandler({
        (NSError error) -> Void in
      if error != nil {
        println("Could not connect to GCM: \(error.localizedDescription)")
      } else {
        self.connectedToGCM = true
        println("Connected to GCM")
      }
    })
  }
  // [END connect_gcm_service]

  // [START disconnect_gcm_service]
  func applicationDidEnterBackground(application: UIApplication) {
    GCMService.sharedInstance().disconnect()
    self.connectedToGCM = false
  }
  // [END disconnect_gcm_service]

  // [START receive_apns_token]
  func application( application: UIApplication!, didRegisterForRemoteNotificationsWithDeviceToken
      deviceToken: NSData! ) {
  // [END receive_apns_token]
        // [START get_gcm_reg_token]
        GMSInstanceID.sharedInstance().startWithConfig(GMSInstanceIDConfig.defaultConfig())
        registrationOptions = [kGMSInstanceIDRegisterAPNSOption:deviceToken!,
          kGMSInstanceIDAPNSServerTypeSandboxOption:true]
        GMSInstanceID.sharedInstance().tokenWithAuthorizedEntity(gcmSenderID,
          scope: kGMSInstanceIDScopeGCM, options: registrationOptions, handler: registrationHandler)
        // [END get_gcm_reg_token]
  }

  // [START receive_apns_token_error]
  func application( application: UIApplication!, didFailToRegisterForRemoteNotificationsWithError
      error: NSError! ) {
    println("Registration for remote notification failed with error: \(error.localizedDescription)")
  // [END receive_apns_token_error]
    let userInfo = ["error": error.localizedDescription]
    NSNotificationCenter.defaultCenter().postNotificationName(
        registrationKey, object: nil, userInfo: userInfo)
  }

  // [START ack_message_reception]
  func application( application: UIApplication,
    didReceiveRemoteNotification userInfo: [NSObject : AnyObject]) {
      println("Notification received: \(userInfo)")
      GCMService.sharedInstance().appDidReceiveMessage(userInfo);
  // [END ack_message_reception]
      NSNotificationCenter.defaultCenter().postNotificationName(messageKey, object: nil,
          userInfo: userInfo)
  }

  func registrationHandler(registrationToken: String!, error: NSError!) {
    if (registrationToken != nil) {
      self.registrationToken = registrationToken
      GCMPubSub().subscribeWithToken(registrationToken, topic: subscriptionTopic, options: nil,
          handler: { (NSError error) -> Void in
        if error != nil {
          println("Subscribed to \(self.subscriptionTopic)")
        } else {
          println("Subcription failed: \(error.localizedDescription)")
        }
      })
      println("Registration Token: \(registrationToken)")
      let userInfo = ["registrationToken": registrationToken]
      NSNotificationCenter.defaultCenter().postNotificationName(
        self.registrationKey, object: nil, userInfo: userInfo)
    } else {
      println("Registration to GCM failed with error: \(error.localizedDescription)")
      let userInfo = ["error": error.localizedDescription]
      NSNotificationCenter.defaultCenter().postNotificationName(
        self.registrationKey, object: nil, userInfo: userInfo)
    }
  }

  // [START on_token_refresh]
  func onTokenRefresh(updateID: Bool) {
    println("The GCM registration token needs to be changed.")
    GMSInstanceID.sharedInstance().tokenWithAuthorizedEntity(gcmSenderID,
        scope: kGMSInstanceIDScopeGCM, options: registrationOptions, handler: registrationHandler)
  }
  // [END on_token_refresh]

}
