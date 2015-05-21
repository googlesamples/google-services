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

#import <UIKit/UIKit.h>
#import <XCTest/XCTest.h>

#import <Google/Analytics.h>

#import "LogWrapper.h"

static NSString * const kSearchTerm = @": Sending hit(s) ";

@interface AppTests : XCTestCase
@end

@implementation AppTests {
  LogWrapper *_logWrapper;
  UITabBarController *_patternsController;
  GAI *_gai;
  GAILogLevel _previousLogLevel;
}

- (instancetype)init {
  if ((self = [super init])) {
    _gai = [GAI sharedInstance];
  }
  return self;
}

- (void)setUp {
  _logWrapper = [[LogWrapper alloc] init];
  _previousLogLevel = _gai.logger.logLevel;
  _gai.logger.logLevel = kGAILogLevelVerbose;  // force verbose logging for test

  UIWindow *window = [UIApplication sharedApplication].keyWindow;
  UIViewController *rootViewController = window.rootViewController;
  for (UIViewController *candidate in rootViewController.childViewControllers) {
    if ([candidate isKindOfClass:[UITabBarController class]]) {
      _patternsController = (UITabBarController *)candidate;
      break;
    }
  }
}

- (void)tearDown {
  _logWrapper = nil;
  _gai.logger.logLevel = _previousLogLevel;
}

- (void)testLogs {
  XCTestExpectation *expectation =
      [self expectationWithDescription:@"Send event inside logs"];
  [_logWrapper lines];  // force clear

  // Trigger a new, different tab to be shown.
  XCTAssertEqual(_patternsController.selectedIndex, 0,
                 @"Expected left-most index to be initially selected");
  _patternsController.selectedIndex = 2;

  // Check the logs constantly until a send message is found.
  NSTimer *timer = [NSTimer scheduledTimerWithTimeInterval:0.1
                                                    target:self
                                                  selector:@selector(checkTimer:)
                                                  userInfo:expectation
                                                   repeats:YES];
  [self waitForExpectationsWithTimeout:10.0 handler:nil];
  [timer invalidate];
}

- (void)checkTimer:(NSTimer *)timer {
  XCTestExpectation *expectation = timer.userInfo;
  NSArray *lines = [_logWrapper lines];

  for (NSString *line in lines) {
    if ([line containsString:kSearchTerm]) {
      [expectation fulfill];
      break;
    }
  }

}

@end
