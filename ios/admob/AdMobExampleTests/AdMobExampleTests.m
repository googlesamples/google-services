//
// Copyright (c) 2015 Google Inc.
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
//  AdMobExample_Tests.m
//  AdMobExample Tests
//

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>
#import "ViewController_Private.h"

@interface AdMobExampleTests : XCTestCase
@property (nonatomic) ViewController *vcToTest;
@property (nonatomic) UIStoryboard *storyboard;
@end

@implementation AdMobExampleTests

- (void)setUp {
  [super setUp];
  UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
  self.vcToTest = [storyboard instantiateViewControllerWithIdentifier:@"ViewController"];
  [self.vcToTest view];
}

- (void)testBannerView {
  XCTAssertNotNil(self.vcToTest.bannerView, @"Should connect bannerView IBOutlet.");
}

- (void)testInterstitial {
  XCTAssertNotNil(self.vcToTest.interstitial, @"Should create interstitial.");
}

@end
