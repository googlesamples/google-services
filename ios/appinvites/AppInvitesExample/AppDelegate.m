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
#import <Google/AppInvite.h>


@implementation AppDelegate

//#error Replace the values here and remove this line to run the sample.
// [START didfinishlaunching]
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:
      (NSDictionary *)launchOptions {
  NSError* configureError;
  [[GGLContext sharedInstance] configureWithError:&configureError];
  if (configureError != nil) {
    NSLog(@"Error configuring the Google context: %@", configureError);
  }

  [GINInvite applicationDidFinishLaunching];
  return YES;
}
// [END didfinishlaunching]

// [START openurl]
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {

  // Handle App Invite requests
  GINReceivedInvite *invite = [GINInvite handleURL:url
                                 sourceApplication:sourceApplication
                                        annotation:annotation];
  if (invite) {
    NSString *message =
    [NSString stringWithFormat:
        @"Deep link from %@ \nInvite ID: %@\nApp URL: %@",
        sourceApplication, invite.inviteId, invite.deepLink];
    [[[UIAlertView alloc] initWithTitle:@"Deep-link Data"
                                message:message
                               delegate:nil
                      cancelButtonTitle:@"OK"
                      otherButtonTitles:nil] show];
    return YES;
  }

  return [[GIDSignIn sharedInstance] handleURL:url
                             sourceApplication:sourceApplication
                                    annotation:annotation];
}
// [END openurl]

@end
