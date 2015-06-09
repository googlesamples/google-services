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

#import <Google/Analytics.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  // [START tracker_objc]
  // Configure tracker from GoogleService-Info.plist.
  NSError *configureError;
  [[GGLContext sharedInstance] configureWithError:&configureError];
  NSAssert(!configureError, @"Error configuring Google services: %@", configureError);

  // Optional: configure GAI options.
  GAI *gai = [GAI sharedInstance];
  gai.trackUncaughtExceptions = YES;  // report uncaught exceptions
  gai.logger.logLevel = kGAILogLevelVerbose;  // remove before app release
  // [END tracker_objc]

  // Set a white background so that patterns are showcased.
  _window.backgroundColor = [UIColor whiteColor];

  return YES;
}

@end
