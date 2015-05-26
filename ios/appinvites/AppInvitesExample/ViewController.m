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

#import <Google/AppInvite.h>
#import <GoogleSignIn/GIDGoogleUser.h>
#import <GoogleSignIn/GIDProfileData.h>
#import <GoogleSignIn/GIDSignIn.h>
#import <GoogleSignIn/GIDSignInButton.h>

// [START viewcontroller_interfaces]
@interface ViewController () <GINInviteDelegate, GIDSignInDelegate, GIDSignInUIDelegate>
// [END viewcontroller_interfaces]
// [START viewcontroller_vars]
@property (weak, nonatomic) IBOutlet GIDSignInButton *signInButton;
@property (weak, nonatomic) IBOutlet UIButton *signOutButton;
@property (weak, nonatomic) IBOutlet UIButton *disconnectButton;
@property (weak, nonatomic) IBOutlet UIButton *inviteButton;
@property (weak, nonatomic) IBOutlet UILabel *statusText;
// [END viewcontroler_vars]
@property (strong, nonatomic) id<GINInviteBuilder> inviteDialog;
@end

@implementation ViewController

// [START viewdidload]
- (void)viewDidLoad {
  [super viewDidLoad];

  // TODO(developer) Configure the sign-in button look/feel
  [GIDSignIn sharedInstance].delegate = self;
  [GIDSignIn sharedInstance].uiDelegate = self;

  // Sign in automatically.
  [[GIDSignIn sharedInstance] signInSilently];

  [self setupUI];
  [self toggleAuthUI];
}
// [END viewdidload]

- (void)setupUI {
  float grayValue = (204.0/255);
  UIColor* grayColor = [UIColor colorWithRed:grayValue
                                       green:grayValue
                                        blue:grayValue
                                       alpha:1.0];

  self.inviteButton.layer.cornerRadius = 3;
  self.inviteButton.layer.shadowRadius = 1;
  self.inviteButton.layer.shadowOffset = CGSizeMake(0, 0.5);
  self.inviteButton.layer.shadowColor = [UIColor blackColor].CGColor;
  self.inviteButton.layer.shadowOpacity = .7;

  self.signOutButton.layer.borderWidth = .5;
  self.signOutButton.layer.borderColor = grayColor.CGColor;
  self.signOutButton.layer.cornerRadius = 2;
  self.signOutButton.layer.shadowRadius = .5;
  self.signOutButton.layer.shadowOffset = CGSizeMake(0, 0.5);
  self.signOutButton.layer.shadowColor = [UIColor blackColor].CGColor;
  self.signOutButton.layer.shadowOpacity = .4;

  self.disconnectButton.layer.borderWidth = .5;
  self.disconnectButton.layer.borderColor = grayColor.CGColor;
  self.disconnectButton.layer.cornerRadius = 2;
  self.disconnectButton.layer.shadowRadius = .5;
  self.disconnectButton.layer.shadowOffset = CGSizeMake(0, 0.5);
  self.disconnectButton.layer.shadowColor = [UIColor blackColor].CGColor;
  self.disconnectButton.layer.shadowOpacity = .4;
}

// [START signin_handler]
- (void)signIn:(GIDSignIn *)signIn didSignInForUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations on signed in user here.
  self.statusText.text = [NSString stringWithFormat:@"Signed in as %@",
                          user.profile.name];
  [self toggleAuthUI];
}
// [END signin_handler]

// [START disconnect_handler]
- (void)signIn:(GIDSignIn *)signIn
didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations when the user disconnects from app here.
  self.statusText.text = @"Disconnected user";
  [self toggleAuthUI];
}
// [END disconnect_handler]

// [START signout_tapped]
- (IBAction)signOutTapped:(id)sender {
  [[GIDSignIn sharedInstance] signOut];
  self.statusText.text = @"Signed out";
  [self toggleAuthUI];
}
// [END signout_tapped]

// [START disconnect_tapped]
- (IBAction)disconnectTapped:(id)sender {
  [[GIDSignIn sharedInstance] disconnect];
}
// [END disconnect_tapped]

// [START invite_tapped]
- (IBAction)inviteTapped:(id)sender {
  self.inviteDialog = [GINInvite inviteDialog];
  [self.inviteDialog setInviteDelegate: self];

  // NOTE: You must have the App Store ID set in your developer console project
  // in order for invitations to successfully be sent.
  NSString* message = [NSString stringWithFormat:@"Try this out!\n -%@",
                       [[GIDSignIn sharedInstance] currentUser].profile.name];

  // A message hint for the dialog. Note this manifests differently depending on the
  // received invation type. For example, in an email invite this appears as the subject.
  [self.inviteDialog setMessage: message];

  // Title for the dialog, this is what the user sees before sending the invites.
  [self.inviteDialog setTitle: @"App Invites Example"];
  [self.inviteDialog setDeepLink: @"app_url"];
  [self.inviteDialog open];
}
// [END invite_tapped]

// [START invite_finished]
- (void)inviteFinishedWithInvitations:(NSArray *)invitationIds
                                error:(NSError *)error {
  NSString *message = error ? error.localizedDescription :
    [NSString stringWithFormat:@"%lu invites sent", (unsigned long)invitationIds.count];
  [[[UIAlertView alloc] initWithTitle:@"Done"
                              message:message
                             delegate:nil
                    cancelButtonTitle:@"OK"
                    otherButtonTitles:nil] show];
}
// [END invite_finished]

// [START toggle_auth]
- (void)toggleAuthUI {
  if ([GIDSignIn sharedInstance].currentUser.authentication == nil) {
    // Not signed in
    self.signInButton.enabled = YES;
    self.signOutButton.enabled = NO;
    self.disconnectButton.enabled = NO;
    self.inviteButton.enabled = NO;
    [self performSegueWithIdentifier:@"SignedOutScreen" sender:self];
  } else {
    // Signed in
    self.signInButton.enabled = NO;
    self.signOutButton.enabled = YES;
    self.disconnectButton.enabled = YES;
    self.inviteButton.enabled = YES;
  }
}
// [END toggle_auth]

- (UIStatusBarStyle)preferredStatusBarStyle {
  return UIStatusBarStyleLightContent;
}
@end
