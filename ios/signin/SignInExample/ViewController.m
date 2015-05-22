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
#import <GoogleSignIn/GIDGoogleUser.h>
#import <GoogleSignIn/GIDProfileData.h>

// [START viewcontroller_interfaces]
@interface ViewController () <GIDSignInDelegate, GIDSignInUIDelegate>
// [END viewcontroller_interfaces]

// [START viewcontroller_vars]
@property (weak, nonatomic) IBOutlet GIDSignInButton *signInButton;
@property (weak, nonatomic) IBOutlet UIButton *signOutButton;
@property (weak, nonatomic) IBOutlet UIButton *disconnectButton;
@property (weak, nonatomic) IBOutlet UILabel *statusText;
// [END viewcontroller_vars]

@end

@implementation ViewController

// [START viewdidload]
- (void)viewDidLoad {
  [super viewDidLoad];

  // TODO(developer) Configure the sign-in button look/feel
  [GIDSignIn sharedInstance].delegate = self;
  [GIDSignIn sharedInstance].uiDelegate = self;

  // Uncomment to automatically sign in the user.
  [[GIDSignIn sharedInstance] signInSilently];
  [self toggleAuthUI];

  [self statusText].text = @"Google Sign in\niOS Demo";
}
// [END viewdidload]

// [START signin_handler]
- (void)signIn:(GIDSignIn *)signIn
didSignInForUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations on signed in user here.
  self.statusText.text = [NSString stringWithFormat:@"Signed in user: %@",
                          user.profile.name];
  [self toggleAuthUI];
}
// [END signin_handler]

// This callback is triggered after the disconnect call that revokes data
// access to the user's resources has completed.
// [START disconnect_handler]
- (void)signIn:(GIDSignIn *)signIn
didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations when the user disconnects from app here.
  self.statusText.text = @"Disconnected user";
  [self toggleAuthUI];
}
// [END disconnect_handler]

// Signs the user out of the application for scenarios such as switching
// profiles.
// [START signout_tapped]
- (IBAction)didTapSignOut:(id)sender {
  [[GIDSignIn sharedInstance] signOut];
  [self toggleAuthUI];
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
@end
