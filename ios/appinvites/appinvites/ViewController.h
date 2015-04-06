//
//  ViewController.h
//  appinvites
//
//  Created by Gus Class on 4/3/15.
//  Copyright (c) 2015 gusclass. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <GoogleSignIn/GIDSignIn.h>
#import <GoogleSignIn/GIDSignInButton.h>

@interface ViewController : UIViewController <GIDSignInDelegate>
@property (weak, nonatomic) IBOutlet UIButton *signInButton;
@property (weak, nonatomic) IBOutlet UIButton *signOutButton;
@property (weak, nonatomic) IBOutlet UIButton *disconnectButton;
@property (weak, nonatomic) IBOutlet UIButton *inviteButton;

@property (weak, nonatomic) IBOutlet GIDSignInButton *SignInView;
@property (weak, nonatomic) IBOutlet UILabel *statusText;
@end

