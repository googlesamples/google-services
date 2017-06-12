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
#import "ViewController.h"

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(updateRegistrationStatus:)
                                               name:appDelegate.registrationKey
                                             object:nil];
  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(showReceivedMessage:)
                                               name:appDelegate.messageKey
                                             object:nil];
  _registrationProgressing.hidesWhenStopped = YES;
  [_registrationProgressing startAnimating];
}

- (void) updateRegistrationStatus:(NSNotification *) notification {
  [_registrationProgressing stopAnimating];
  if ([notification.userInfo objectForKey:@"error"]) {
    _registeringLabel.text = @"Error registering!";
    [self showAlert:@"Error registering with GCM" withMessage:notification.userInfo[@"error"]];
  } else {
    _registeringLabel.text = @"Registered!";
    NSString *message = @"Check the xcode debug console for the registration token that you can"
        " use with the demo server to send notifications to your device";
    [self showAlert:@"Registration Successful" withMessage:message];
  };
}

- (void) showReceivedMessage:(NSNotification *) notification {
  NSString *message = notification.userInfo[@"aps"][@"alert"];
  [self showAlert:@"Message received" withMessage:message];
}

- (void)showAlert:(NSString *)title withMessage:(NSString *) message{
  if (floor(NSFoundationVersionNumber) <= NSFoundationVersionNumber_iOS_7_1) {
    // iOS 7.1 or earlier
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                    message:message
                                                   delegate:nil
                                          cancelButtonTitle:@"Dismiss"
                                          otherButtonTitles:nil];
    [alert show];
  } else {
    //iOS 8 or later
    UIAlertController *alert =
        [UIAlertController alertControllerWithTitle:title
                                            message:message
                                     preferredStyle:UIAlertControllerStyleAlert];

    UIAlertAction *dismissAction = [UIAlertAction actionWithTitle:@"Dismiss"
                                                            style:UIAlertActionStyleDestructive
                                                          handler:nil];

    [alert addAction:dismissAction];
    [self presentViewController:alert animated:YES completion:nil];
  }
}

- (UIStatusBarStyle)preferredStatusBarStyle {
  return UIStatusBarStyleLightContent;
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
