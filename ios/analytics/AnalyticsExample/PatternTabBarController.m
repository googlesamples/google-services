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

#import "PatternTabBarController.h"

#import <Google/Analytics.h>

@implementation PatternTabBarController

- (void)didTapShare:(id)sender {
  // [START custom_event_objc]
  id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
  NSMutableDictionary *event =
      [[GAIDictionaryBuilder createEventWithCategory:@"Action"
                                              action:@"Share"
                                               label:nil
                                               value:nil] build];
  [tracker send:event];
  // [END custom_event_objc]

  NSString *title = [NSString stringWithFormat:@"Share: %@",
                     self.selectedViewController.title];
  NSString *message =
      @"Share event sent to Analytics; actual share not implemented in this quickstart";
  UIAlertView *alert = [[UIAlertView alloc] initWithTitle:title
                                                  message:message
                                                 delegate:nil
                                        cancelButtonTitle:@"Ok"
                                        otherButtonTitles:nil];
  [alert show];
}

@end
