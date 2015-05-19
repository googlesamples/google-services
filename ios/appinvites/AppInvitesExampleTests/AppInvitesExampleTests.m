//
//  Copyright (c) Google Inc.
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
#import <Google/AppInvite.h>

@interface AppInvitesExampleTests : XCTestCase

@end

@implementation AppInvitesExampleTests

- (void)setUp {
  [super setUp];
  // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
  // Put teardown code here. This method is called after the invocation of each test method in the class.
  [super tearDown];
}

- (void)testAppWasConfigured {
  // Check that the GGL context matches what we read from the file.
  GGLContext* context = [GGLContext sharedInstance];
  NSDictionary *configPlist =
      [NSDictionary dictionaryWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"GoogleService-Info" ofType:@"plist"]];

  NSError* configureError = nil;
  [context configureWithError: &configureError];

  // Check the client ID is configured for Android target app.
  NSString* actual = context.configuration.clientID;
  NSString* expected = [configPlist objectForKey:@"CLIENT_ID"];

  XCTAssertEqualObjects(actual, expected);
}

@end
