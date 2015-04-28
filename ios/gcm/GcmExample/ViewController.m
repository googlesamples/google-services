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

#import <GMPInstanceID.h>

#import "ViewController.h"

// TODO(silvano): move to info.plist
static NSString *const senderID = @"177545629583";

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  _registrationProgressing.hidesWhenStopped = YES;
  [_registrationProgressing startAnimating];
}

- (void) didReceiveAPNSToken:(NSData *)apnsDeviceToken {
  NSDictionary *options = @{kGMPInstanceIDRegisterAPNSOption: apnsDeviceToken,
                            kGMPInstanceIDAPNSServerTypeSandboxOption: @YES};
  GMPInstanceIDTokenHandler registrationHandler = ^void(NSString *registrationId, NSError *error) {
      if (registrationId != nil) {
        [_registrationProgressing stopAnimating];
        _registeringLabel.text = @"Registered!";
        NSLog(@"Registration ID: %@", registrationId);
        NSString *message = @"Check the xcode debug console for the registration ID that you can"
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
      } else {
        NSLog(@"Registration to GCM failed with error: %@", error);
        UIAlertController *alert =
            [UIAlertController alertControllerWithTitle: @"Error registering with GCM"
                                                message: error.localizedDescription
                                         preferredStyle: UIAlertControllerStyleAlert];

        UIAlertAction *dismissAction = [UIAlertAction actionWithTitle: @"Dismiss"
                                                                style: UIAlertActionStyleDestructive
                                                              handler: nil];

        [alert addAction: dismissAction];
        [self presentViewController: alert animated: YES completion: nil];
      }
  };
  [[GMPInstanceID sharedInstance] tokenWithAudience:senderID
                                              scope:kGMPInstanceIDScopeGCM
                                            options:options
                                            handler:registrationHandler];
}

@end
