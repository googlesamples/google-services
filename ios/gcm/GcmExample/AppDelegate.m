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

#import "AppDelegate.h"

// TODO(silvano): move to info.plist
static NSString *const senderID = @"177545629583";

@implementation AppDelegate

// [START register_for_remote_notifications]
- (BOOL)application:(UIApplication *)application
      didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  _notificationKey = @"onRegistrationCompleted";
  UIUserNotificationType allNotificationTypes =
      (UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge);
  UIUserNotificationSettings *settings =
      [UIUserNotificationSettings settingsForTypes:allNotificationTypes categories:nil];
  [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
  [[UIApplication sharedApplication] registerForRemoteNotifications];
  // [END register_for_remote_notifications]
  // [START start_gcm_service]
  [[GCMService sharedInstance] startWithConfig: [GCMConfig defaultConfig]];
  // [END start_gcm_service]
  return YES;
}

// [START connect_gcm_service]
- (void)applicationDidBecomeActive:(UIApplication *)application {
  [[GCMService sharedInstance] connectWithHandler:^(NSError *error) {
    if (error) {
      NSLog(@"Could not connect to GCM: %@", error.localizedDescription);
    } else {
      NSLog(@"Connected to GCM");
    }
  }];
}
// [END connect_gcm_service]

// [START disconnect_gcm_service]
- (void)applicationDidEnterBackground:(UIApplication *)application {
  [[GCMService sharedInstance] disconnect];
}
// [END disconnect_gcm_service]

// [START receive_apns_token]
- (void)application:(UIApplication *)application
    didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
// [END receive_apns_token]
  // [START get_gcm_reg_token]
  [[GMPInstanceID sharedInstance] startWithConfig:[GMPInstanceIDConfig defaultConfig]];
  NSDictionary *options = @{kGMPInstanceIDRegisterAPNSOption: deviceToken,
                            kGMPInstanceIDAPNSServerTypeSandboxOption: @YES};
  GMPInstanceIDTokenHandler registrationHandler = ^void(NSString *registrationToken,
                                                        NSError *error) {
    if (registrationToken != nil) {
      NSLog(@"Registration Token: %@", registrationToken);
      NSDictionary *userInfo = @{@"registrationToken" : registrationToken};
      [[NSNotificationCenter defaultCenter] postNotificationName: _notificationKey
                                                          object: nil
                                                        userInfo: userInfo];
    } else {
      NSLog(@"Registration to GCM failed with error: %@", error.localizedDescription);
      NSDictionary *userInfo = @{@"error" : error.localizedDescription};
      [[NSNotificationCenter defaultCenter] postNotificationName: _notificationKey
                                                          object: nil
                                                        userInfo: userInfo];
    }
  };
  [[GMPInstanceID sharedInstance] tokenWithAudience:senderID
                                              scope:kGMPInstanceIDScopeGCM
                                            options:options
                                            handler:registrationHandler];
  // [END get_gcm_reg_token]
}

// [START receive_apns_token_error]
- (void)application:(UIApplication *)application
    didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
  NSLog(@"Registration for remote notification failed with error: %@", error.localizedDescription);
// [END receive_apns_token_error]
  NSDictionary *userInfo = @{@"error" : error.localizedDescription};
  [[NSNotificationCenter defaultCenter] postNotificationName: _notificationKey
                                                      object: nil
                                                    userInfo: userInfo];
}

- (void)application:(UIApplication *)application
    didReceiveRemoteNotification:(NSDictionary *)userInfo {
  NSLog(@"Notification received: %@", userInfo);
}

@end
