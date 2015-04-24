//
//  ViewController.m
//  signin
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
#import <GoogleSignIn/GIDGoogleUser.h>
#import <GoogleSignIn/GIDProfileData.h>

@interface ViewController () <GIDSignInDelegate>

@property (weak, nonatomic) IBOutlet GIDSignInButton *signInButton;
@property (weak, nonatomic) IBOutlet UIButton *signOutButton;
@property (weak, nonatomic) IBOutlet UIButton *disconnectButton;
@property (weak, nonatomic) IBOutlet GIDSignInButton *SignInView;
@property (weak, nonatomic) IBOutlet UILabel *statusText;

@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  // TODO(developer) Configure the sign-in button look/feel
  [GIDSignIn sharedInstance].delegate = self;

  [[GIDSignIn sharedInstance] signInSilently];
  [self toggleAuthUI];
}

- (void)signIn:(GIDSignIn *)signIn
didSignInForUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations on signed in user here.
  self.statusText.text = [NSString stringWithFormat:@"Signed in user: %@",
                          user.profile.name];
  [self toggleAuthUI];
}

- (void)signIn:(GIDSignIn *)signIn
didDisconnectWithUser:(GIDGoogleUser *)user
     withError:(NSError *)error {
  // Perform any operations when the user disconnects from app here.
  self.statusText.text = @"Disconnected user";
  [self toggleAuthUI];
}

- (IBAction)signOutTapped:(id)sender {
  [[GIDSignIn sharedInstance] signOut];
  [self toggleAuthUI];
}

- (IBAction)disconnectTapped:(id)sender {
  [[GIDSignIn sharedInstance] disconnect];
}

- (void)toggleAuthUI {
  if ([GIDSignIn sharedInstance].currentUser.authentication == nil) {
    // Not signed in
    self.signInButton.enabled = YES;
    self.signOutButton.enabled = NO;
    self.disconnectButton.enabled = NO;
  }else{
    // Signed in
    self.signInButton.enabled = NO;
    self.signOutButton.enabled = YES;
    self.disconnectButton.enabled = YES;
  }
}

@end
