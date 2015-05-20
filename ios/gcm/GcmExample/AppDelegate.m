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

@interface AppDelegate ()
@property(nonatomic, strong) void (^registrationHandler)
    (NSString *registrationToken, NSError *error);
@property(nonatomic, assign) BOOL connectedToGCM;
@property(nonatomic, strong) NSString* registrationToken;
@property(nonatomic, assign) BOOL subscribedToTopic;
@end

NSString *const SubscriptionTopic = @"/topics/global";

@implementation AppDelegate

// [START register_for_remote_notifications]
- (BOOL)application:(UIApplication *)application
      didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  _registrationKey = @"onRegistrationCompleted";
  _messageKey = @"onMessageReceived";
  NSError* configureError;
  [[GGLContext sharedInstance] configureWithError:&configureError];
  if (configureError != nil) {
    NSLog(@"Error configuring the Google context: %@", configureError);
  }
  _gcmSenderID = [[[GGLContext sharedInstance] configuration] gcmSenderID];
  UIUserNotificationType allNotificationTypes =
      (UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge);
  UIUserNotificationSettings *settings =
      [UIUserNotificationSettings settingsForTypes:allNotificationTypes categories:nil];
  [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
  [[UIApplication sharedApplication] registerForRemoteNotifications];
  // [END register_for_remote_notifications]
  // [START start_gcm_service]
  [[GCMService sharedInstance] startWithConfig:[GCMConfig defaultConfig]];
  // [END start_gcm_service]
  __weak typeof(self) weakSelf = self;
  _registrationHandler = ^(NSString *registrationToken, NSError *error){
    if (registrationToken != nil) {
      weakSelf.registrationToken = registrationToken;
      NSLog(@"Registration Token: %@", registrationToken);
      NSDictionary *userInfo = @{@"registrationToken":registrationToken};
      [[NSNotificationCenter defaultCenter] postNotificationName:weakSelf.registrationKey
                                                          object:nil
                                                        userInfo:userInfo];
    } else {
      NSLog(@"Registration to GCM failed with error: %@", error.localizedDescription);
      NSDictionary *userInfo = @{@"error":error.localizedDescription};
      [[NSNotificationCenter defaultCenter] postNotificationName:weakSelf.registrationKey
                                                          object:nil
                                                        userInfo:userInfo];
    }
  };
  return YES;
}

- (void)subscribeToTopic {
  if (_registrationToken && _connectedToGCM) {
    [[GCMPubSub sharedInstance] subscribeWithToken:_registrationToken
                                             topic:SubscriptionTopic
                                           options:nil
                                           handler:^(NSError *error) {
                                             if (error) {
                                               NSLog(@"Subcription failed: %@",
                                                     error.localizedDescription);
                                             } else {
                                               self.subscribedToTopic = true;
                                               NSLog(@"Subscribed to %@", SubscriptionTopic);
                                             }
                                           }];
  }
}

// [START connect_gcm_service]
- (void)applicationDidBecomeActive:(UIApplication *)application {
  [[GCMService sharedInstance] connectWithHandler:^(NSError *error) {
    if (error) {
      NSLog(@"Could not connect to GCM: %@", error.localizedDescription);
    } else {
      _connectedToGCM = true;
      NSLog(@"Connected to GCM");
    }
  }];
}
// [END connect_gcm_service]

// [START disconnect_gcm_service]
- (void)applicationDidEnterBackground:(UIApplication *)application {
  [[GCMService sharedInstance] disconnect];
  _connectedToGCM = NO;
}
// [END disconnect_gcm_service]

// [START receive_apns_token]
- (void)application:(UIApplication *)application
    didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
// [END receive_apns_token]
  // [START get_gcm_reg_token]
  [[GMSInstanceID sharedInstance] startWithConfig:[GMSInstanceIDConfig defaultConfig]];
  _registrationOptions = @{kGMSInstanceIDRegisterAPNSOption:deviceToken,
                           kGMSInstanceIDAPNSServerTypeSandboxOption:@YES};
  [[GMSInstanceID sharedInstance] tokenWithAuthorizedEntity:_gcmSenderID
                                                      scope:kGMSInstanceIDScopeGCM
                                                    options:_registrationOptions
                                                    handler:_registrationHandler];
  // [END get_gcm_reg_token]
}

// [START receive_apns_token_error]
- (void)application:(UIApplication *)application
    didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
  NSLog(@"Registration for remote notification failed with error: %@", error.localizedDescription);
// [END receive_apns_token_error]
  NSDictionary *userInfo = @{@"error" :error.localizedDescription};
  [[NSNotificationCenter defaultCenter] postNotificationName:_registrationKey
                                                      object:nil
                                                    userInfo:userInfo];
}

// [START ack_message_reception]
- (void)application:(UIApplication *)application
    didReceiveRemoteNotification:(NSDictionary *)userInfo {
  NSLog(@"Notification received: %@", userInfo);
  [[GCMService sharedInstance] appDidReceiveMessage:userInfo];
// [END ack_message_reception]
  [[NSNotificationCenter defaultCenter] postNotificationName:_messageKey
                                                      object:nil
                                                    userInfo:userInfo];

}

// [START on_token_refresh]
- (void)onTokenRefresh:(BOOL)updateID {
  NSLog(@"The GCM registration token needs to be changed.");
  [[GMSInstanceID sharedInstance] tokenWithAuthorizedEntity:_gcmSenderID
                                                      scope:kGMSInstanceIDScopeGCM
                                                    options:_registrationOptions
                                                    handler:_registrationHandler];
}
// [END on_token_refresh]

@end
