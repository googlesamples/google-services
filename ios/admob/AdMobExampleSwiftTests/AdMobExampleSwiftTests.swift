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
//  AdMobExampleSwift_Tests.swift
//  AdMobExampleSwift Tests
//

import UIKit
import XCTest

//  Important: Import the project module
import AdMobExampleSwift

class AdMobExampleSwiftTests: XCTestCase {

  // Important: create a variable for the view controller outside of setup(). Otherwise the variable is not available to the test functions
  var vcToTest:ViewController = ViewController()

  override func setUp() {
    super.setUp()
    let storyboard:UIStoryboard = UIStoryboard(name: "Main", bundle: NSBundle(forClass: self.dynamicType))
    vcToTest = storyboard.instantiateViewControllerWithIdentifier("ViewController") as! ViewController
    let view = vcToTest.view
  }

  func testBannerView() {
    XCTAssertNotNil(vcToTest.bannerView, "Should connect bannerView IBOutlet.")
  }

  func testInterstitial() {
    XCTAssertNotNil(vcToTest.interstitial, "Should create interstitial.")
  }

}
