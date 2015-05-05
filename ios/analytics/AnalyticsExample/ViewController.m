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

#import <GoogleMobilePlatform/Analytics.h>

@implementation ViewController {
  UIColor *_color;
}

- (void)viewWillAppear:(BOOL)animated {
  [super viewWillAppear:animated];

  NSString *name = [NSString stringWithFormat:@"Pattern~%@", self.title];

  // The UA-XXXXX-Y tracker ID is loaded automatically from the
  // GoogleService-Info.plist by the `GHIContext` in the AppDelegate.
  // If you're copying this to an app just using Analytics, you'll
  // need to configure your tracking ID here.
  // [START screen_view_event_objc]
  id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
  [tracker set:kGAIScreenName value:name];
  [tracker send:[[GAIDictionaryBuilder createScreenView] build]];
  // [END screen_view_event_objc]
}

- (void)viewDidAppear:(BOOL)animated {
  self.view.backgroundColor = _color;
}

- (void)setBackground:(UIImageView *)background {
  _background = background;

  UIImage *image = _background.image;
  _color = image ? [UIColor colorWithPatternImage:image] : nil;
}

@end
