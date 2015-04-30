//
//  ViewController.m
//  appinvites
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

#import "ViewController.h"
#import <GINInvite/GINInvite.h>
#import <GoogleSignIn/GIDGoogleUser.h>
#import <GoogleSignIn/GIDProfileData.h>

// [START viewcontroller_interfaces]
@interface ViewController () <GINInviteDelegate, GIDSignInDelegate>
// [END viewcontroller_interfaces]
// [START viewcontroller_vars]
@property (weak, nonatomic) IBOutlet GIDSignInButton *signInButton;
@property (weak, nonatomic) IBOutlet UIButton *signOutButton;
@property (weak, nonatomic) IBOutlet UIButton *disconnectButton;
@property (weak, nonatomic) IBOutlet UIButton *inviteButton;
@property (weak, nonatomic) IBOutlet UILabel *statusText;
// [END viewcontroler_vars]
@end

@implementation ViewController

// [START viewdidload]
- (void)viewDidLoad {
  [super viewDidLoad];

  // TODO(developer) Configure the sign-in button look/feel
  [GIDSignIn sharedInstance].delegate = self;

  // Uncomment to sign in automatically.
  [[GIDSignIn sharedInstance] signInSilently];

  [self toggleAuthUI];
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
    id<GINInviteBuilder> inviteDialog = [GINInvite inviteDialog];
    [inviteDialog setInviteDelegate: self];
    [inviteDialog setMessage: @"message"];
    [inviteDialog setTitle: @"title"];
    [inviteDialog setDeepLink: @"message=yes"];
    [inviteDialog open];
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
  }else{
    // Signed in
    self.signInButton.enabled = NO;
    self.signOutButton.enabled = YES;
    self.disconnectButton.enabled = YES;
    self.inviteButton.enabled = YES;
  }
}
// [END toggle_auth]

@end
