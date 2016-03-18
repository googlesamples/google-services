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
class AppDelegate: UIResponder, UIApplicationDelegate, GGLInstanceIDDelegate, GCMReceiverDelegate {

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
    // Configure the Google context: parses the GoogleService-Info.plist, and initializes
    // the services that have entries in the file
    var configureError:NSError?
    GGLContext.sharedInstance().configureWithError(&configureError)
    assert(configureError == nil, "Error configuring Google services: \(configureError)")
    gcmSenderID = GGLContext.sharedInstance().configuration.gcmSenderID
    // [END_EXCLUDE]
    // Register for remote notifications
    if #available(iOS 8.0, *) {
      let settings: UIUserNotificationSettings =
          UIUserNotificationSettings(forTypes: [.Alert, .Badge, .Sound], categories: nil)
      application.registerUserNotificationSettings(settings)
      application.registerForRemoteNotifications()
    } else {
      // Fallback
      let types: UIRemoteNotificationType = [.Alert, .Badge, .Sound]
      application.registerForRemoteNotificationTypes(types)
    }

  // [END register_for_remote_notifications]
  // [START start_gcm_service]
    let gcmConfig = GCMConfig.defaultConfig()
    gcmConfig.receiverDelegate = self
    GCMService.sharedInstance().startWithConfig(gcmConfig)
  // [END start_gcm_service]
    return true
  }

  func subscribeToTopic() {
    // If the app has a registration token and is connected to GCM, proceed to subscribe to the
    // topic
    if(registrationToken != nil && connectedToGCM) {
      GCMPubSub.sharedInstance().subscribeWithToken(self.registrationToken, topic: subscriptionTopic,
      options: nil, handler: {(error:NSError?) -> Void in
          if let error = error {
            // Treat the "already subscribed" error more gently
            if error.code == 3001 {
              print("Already subscribed to \(self.subscriptionTopic)")
            } else {
              print("Subscription failed: \(error.localizedDescription)");
            }
          } else {
            self.subscribedToTopic = true;
            NSLog("Subscribed to \(self.subscriptionTopic)");
          }
      })
    }
  }

  // [START connect_gcm_service]
  func applicationDidBecomeActive( application: UIApplication) {
    // Connect to the GCM server to receive non-APNS notifications
    GCMService.sharedInstance().connectWithHandler({(error:NSError?) -> Void in
      if let error = error {
        print("Could not connect to GCM: \(error.localizedDescription)")
      } else {
        self.connectedToGCM = true
        print("Connected to GCM")
        // [START_EXCLUDE]
        self.subscribeToTopic()
        // [END_EXCLUDE]
      }
    })
  }
  // [END connect_gcm_service]

  // [START disconnect_gcm_service]
  func applicationDidEnterBackground(application: UIApplication) {
    GCMService.sharedInstance().disconnect()
    // [START_EXCLUDE]
    self.connectedToGCM = false
    // [END_EXCLUDE]
  }
  // [END disconnect_gcm_service]

  // [START receive_apns_token]
  func application( application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken
      deviceToken: NSData ) {
  // [END receive_apns_token]
        // [START get_gcm_reg_token]
        // Create a config and set a delegate that implements the GGLInstaceIDDelegate protocol.
        let instanceIDConfig = GGLInstanceIDConfig.defaultConfig()
        instanceIDConfig.delegate = self
        // Start the GGLInstanceID shared instance with that config and request a registration
        // token to enable reception of notifications
        GGLInstanceID.sharedInstance().startWithConfig(instanceIDConfig)
        registrationOptions = [kGGLInstanceIDRegisterAPNSOption:deviceToken,
          kGGLInstanceIDAPNSServerTypeSandboxOption:true]
        GGLInstanceID.sharedInstance().tokenWithAuthorizedEntity(gcmSenderID,
          scope: kGGLInstanceIDScopeGCM, options: registrationOptions, handler: registrationHandler)
        // [END get_gcm_reg_token]
  }

  // [START receive_apns_token_error]
  func application( application: UIApplication, didFailToRegisterForRemoteNotificationsWithError
      error: NSError ) {
    print("Registration for remote notification failed with error: \(error.localizedDescription)")
  // [END receive_apns_token_error]
    let userInfo = ["error": error.localizedDescription]
    NSNotificationCenter.defaultCenter().postNotificationName(
        registrationKey, object: nil, userInfo: userInfo)
  }

  // [START ack_message_reception]
  func application( application: UIApplication,
    didReceiveRemoteNotification userInfo: [NSObject : AnyObject]) {
      print("Notification received: \(userInfo)")
      // This works only if the app started the GCM service
      GCMService.sharedInstance().appDidReceiveMessage(userInfo);
      // Handle the received message
      // [START_EXCLUDE]
      NSNotificationCenter.defaultCenter().postNotificationName(messageKey, object: nil,
          userInfo: userInfo)
      // [END_EXCLUDE]
  }

  func application( application: UIApplication,
    didReceiveRemoteNotification userInfo: [NSObject : AnyObject],
    fetchCompletionHandler handler: (UIBackgroundFetchResult) -> Void) {
      print("Notification received: \(userInfo)")
      // This works only if the app started the GCM service
      GCMService.sharedInstance().appDidReceiveMessage(userInfo);
      // Handle the received message
      // Invoke the completion handler passing the appropriate UIBackgroundFetchResult value
      // [START_EXCLUDE]
      NSNotificationCenter.defaultCenter().postNotificationName(messageKey, object: nil,
        userInfo: userInfo)
      handler(UIBackgroundFetchResult.NoData);
      // [END_EXCLUDE]
  }
  // [END ack_message_reception]

  func registrationHandler(registrationToken: String!, error: NSError!) {
    if (registrationToken != nil) {
      self.registrationToken = registrationToken
      print("Registration Token: \(registrationToken)")
      self.subscribeToTopic()
      let userInfo = ["registrationToken": registrationToken]
      NSNotificationCenter.defaultCenter().postNotificationName(
        self.registrationKey, object: nil, userInfo: userInfo)
    } else {
      print("Registration to GCM failed with error: \(error.localizedDescription)")
      let userInfo = ["error": error.localizedDescription]
      NSNotificationCenter.defaultCenter().postNotificationName(
        self.registrationKey, object: nil, userInfo: userInfo)
    }
  }

  // [START on_token_refresh]
  func onTokenRefresh() {
    // A rotation of the registration tokens is happening, so the app needs to request a new token.
    print("The GCM registration token needs to be changed.")
    GGLInstanceID.sharedInstance().tokenWithAuthorizedEntity(gcmSenderID,
        scope: kGGLInstanceIDScopeGCM, options: registrationOptions, handler: registrationHandler)
  }
  // [END on_token_refresh]

  // [START upstream_callbacks]
  func willSendDataMessageWithID(messageID: String!, error: NSError!) {
    if (error != nil) {
      // Failed to send the message.
    } else {
      // Will send message, you can save the messageID to track the message
    }
  }

  func didSendDataMessageWithID(messageID: String!) {
    // Did successfully send message identified by messageID
  }
  // [END upstream_callbacks]

  func didDeleteMessagesOnServer() {
    // Some messages sent to this device were deleted on the GCM server before reception, likely
    // because the TTL expired. The client should notify the app server of this, so that the app
    // server can resend those messages.
  }

}
