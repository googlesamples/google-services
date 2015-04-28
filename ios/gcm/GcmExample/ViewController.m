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
  NSString *notificationKey = appDelegate.notificationKey;
  [[NSNotificationCenter defaultCenter] addObserver: self
                                           selector: @selector(updateRegistrationStatus:)
                                               name: notificationKey
                                             object: nil];
  _registrationProgressing.hidesWhenStopped = YES;
  [_registrationProgressing startAnimating];
}

- (void) updateRegistrationStatus:(NSNotification *) notification {
  [_registrationProgressing stopAnimating];
  if ([notification.userInfo objectForKey:@"error"]) {
    _registeringLabel.text = @"Error registering!";
    UIAlertController *alert =
        [UIAlertController alertControllerWithTitle: @"Error registering with GCM"
                                            message: notification.userInfo[@"error"]
                                     preferredStyle: UIAlertControllerStyleAlert];

    UIAlertAction *dismissAction = [UIAlertAction actionWithTitle: @"Dismiss"
                                                            style: UIAlertActionStyleDestructive
                                                          handler: nil];

    [alert addAction: dismissAction];
    [self presentViewController: alert animated: YES completion: nil];
  } else {
    _registeringLabel.text = @"Registered!";
    NSString *message = @"Check the xcode debug console for the registration token that you can"
        " use with the demo server to send notifications to your device";
    UIAlertController *success =
        [UIAlertController alertControllerWithTitle: @"Registration Successful"
                                            message: message
                                     preferredStyle: UIAlertControllerStyleAlert];

    UIAlertAction *dismissAction = [UIAlertAction actionWithTitle: @"Dismiss"
                                                            style: UIAlertActionStyleDestructive
                                                          handler: nil];

    [success addAction: dismissAction];
    [self presentViewController: success animated: YES completion: nil];
  };
}

- (void)dealloc {
  [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
