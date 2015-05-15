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

// [START display_link]
- (void)viewDidLoad {
    [super viewDidLoad];
    
    // This notification is sent the first time the app launches
    [[NSNotificationCenter defaultCenter] addObserver: self
                                             selector: @selector(applicationBecameActive:)
                                                 name: UIApplicationDidBecomeActiveNotification
                                               object: nil];
}

- (void)applicationBecameActive:(NSNotification *)notification {
    AppDelegate *appDelegate = (AppDelegate*)[[UIApplication sharedApplication] delegate];
    self.deepLinkLabel.text = appDelegate.currentDeepLink;
    
}
// [END display_link]

@end
