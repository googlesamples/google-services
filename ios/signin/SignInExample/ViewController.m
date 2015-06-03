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
#import "ViewController.h"

@implementation ViewController

// [START viewdidload]
- (void)viewDidLoad {
  [super viewDidLoad];

  // TODO(developer) Configure the sign-in button look/feel

  [GIDSignIn sharedInstance].uiDelegate = self;

  // Uncomment to automatically sign in the user.
  //[[GIDSignIn sharedInstance] signInSilently];
  // [START_EXCLUDE silent]
  [[NSNotificationCenter defaultCenter]
      addObserver:self
         selector:@selector(receiveToggleAuthUINotification:)
             name:@"ToggleAuthUINotification"
           object:nil];

  [self toggleAuthUI];
  [self statusText].text = @"Google Sign in\niOS Demo";
  // [END_EXCLUDE]
}
// [END viewdidload]

// Signs the user out of the application for scenarios such as switching
// profiles.
// [START signout_tapped]
- (IBAction)didTapSignOut:(id)sender {
  [[GIDSignIn sharedInstance] signOut];
  // [START_EXCLUDE silent]
  [self toggleAuthUI];
  // [END_EXCLUDE]
}
// [END signout_tapped]

// Note: Disconnect revokes access to user data and should only be called in
//     scenarios such as when the user deletes their account. More information
//     on revocation can be found here: https://goo.gl/Gx7oEG.
// [START disconnect_tapped]
- (IBAction)didTapDisconnect:(id)sender {
  [[GIDSignIn sharedInstance] disconnect];
}
// [END disconnect_tapped]

// [START toggle_auth]
- (void)toggleAuthUI {
  if ([GIDSignIn sharedInstance].currentUser.authentication == nil) {
    // Not signed in
    [self statusText].text = @"Google Sign in\niOS Demo";
    self.signInButton.hidden = NO;
    self.signOutButton.hidden = YES;
    self.disconnectButton.hidden = YES;
  } else {
    // Signed in
    self.signInButton.hidden = YES;
    self.signOutButton.hidden = NO;
    self.disconnectButton.hidden = NO;
  }
}
// [END toggle_auth]

- (UIStatusBarStyle)preferredStatusBarStyle {
  return UIStatusBarStyleLightContent;
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter]
      removeObserver:self
                name:@"ToggleAuthUINotification"
              object:nil];

}

- (void) receiveToggleAuthUINotification:(NSNotification *) notification {
  if ([[notification name] isEqualToString:@"ToggleAuthUINotification"]) {
    [self toggleAuthUI];
    self.statusText.text = [notification userInfo][@"statusText"];
  }
}
@end
