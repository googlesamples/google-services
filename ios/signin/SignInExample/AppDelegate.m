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

#import "AppDelegate.h"

@implementation AppDelegate

// [START didfinishlaunching]
- (BOOL)application:(UIApplication *)application
didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  NSError* configureError;
  [[GGLContext sharedInstance] configureWithError: &configureError];
  NSAssert(!configureError, @"Error configuring Google services: %@", configureError);

  [GIDSignIn sharedInstance].delegate = self;

  return YES;
}
// [END didfinishlaunching]

// [START openurl]
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
  return [[GIDSignIn sharedInstance] handleURL:url
                             sourceApplication:sourceApplication
                                    annotation:annotation];
}
// [END openurl]

// [START signin_handler]
- (void)signIn:(GIDSignIn *)signIn
didSignInForUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations on signed in user here.
  NSString *userId = user.userID;                  // For client-side use only!
  NSString *idToken = user.authentication.idToken; // Safe to send to the server
  NSString *name = user.profile.name;
  NSString *email = user.profile.email;
  // [START_EXCLUDE]
  NSDictionary *statusText = @{@"statusText":
                                 [NSString stringWithFormat:@"Signed in user: %@",
                                  name]};
  [[NSNotificationCenter defaultCenter]
   postNotificationName:@"ToggleAuthUINotification"
   object:nil
   userInfo:statusText];
  // [END_EXCLUDE]
}
// [END signin_handler]

// This callback is triggered after the disconnect call that revokes data
// access to the user's resources has completed.
// [START disconnect_handler]
- (void)signIn:(GIDSignIn *)signIn
didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations when the user disconnects from app here.
  // [START_EXCLUDE]
  NSDictionary *statusText = @{@"statusText": @"Disconnected user" };
  [[NSNotificationCenter defaultCenter]
   postNotificationName:@"ToggleAuthUINotification"
   object:nil
   userInfo:statusText];
  // [END_EXCLUDE]
}
// [END disconnect_handler]

@end
