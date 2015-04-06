//
//  ViewController.m
//  appinvites
//
//  Created by Gus Class on 4/3/15.
//  Copyright (c) 2015 gusclass. All rights reserved.
//

#import "ViewController.h"
#import <GPPInvite/GPPInvite.h>
#import <GoogleSignIn/GIDGoogleUser.h>
#import <GoogleSignIn/GIDProfileData.h>

@interface ViewController () <GPPInviteDelegate>

@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  // Configure the sign-in button look/feel
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

- (IBAction)signInTapped:(id)sender {
  [[GIDSignIn sharedInstance] signIn];
  [self toggleAuthUI];
}


- (IBAction)signOutTapped:(id)sender {
  [[GIDSignIn sharedInstance] signOut];
  [self toggleAuthUI];
}

- (IBAction)disconnectTapped:(id)sender {
  [[GIDSignIn sharedInstance] disconnect];
}


- (IBAction)inviteTapped:(id)sender {
    GPPInvite *invite = [GPPInvite sharedInstance];
    [invite setDelegate:self];
    id<GPPInviteBuilder> inviteDialog = [invite inviteDialog];
    [inviteDialog setMessage:@"message"];
    [inviteDialog setTitle:@"title"];
    [inviteDialog setAppURL:@"app_url"];
    [inviteDialog setEmailMessage:@"email message"];
    [inviteDialog open];
}


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

- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}


@end
