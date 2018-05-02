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

#import "ViewController.h"

// [START screen_view_hit_imports_objc]
#import <GoogleAnalytics/GAI.h>
#import <GoogleAnalytics/GAIDictionaryBuilder.h>
#import <GoogleAnalytics/GAIFields.h>
// [END screen_view_hit_imports_objc]

@implementation ViewController

- (void)viewWillAppear:(BOOL)animated {
  [super viewWillAppear:animated];

  NSString *name = [NSString stringWithFormat:@"Pattern~%@", self.title];

  // The UA-XXXXX-Y tracker ID is loaded automatically from the
  // GoogleService-Info.plist by the `GGLContext` in the AppDelegate.
  // If you're copying this to an app just using Analytics, you'll
  // need to configure your tracking ID here.
  // [START screen_view_hit_objc]
  id<GAITracker> tracker = [GAI sharedInstance].defaultTracker;
  [tracker set:kGAIScreenName value:name];
  [tracker send:[[GAIDictionaryBuilder createScreenView] build]];
  // [END screen_view_hit_objc]
}

@end
