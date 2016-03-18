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
//  ViewController.m
//  AdMobExample
//

// [START gmp_banner_example]
#import "ViewController.h"
@import Google.AdMob;

@interface ViewController () <GADInterstitialDelegate>

/**
 * @property
 * A UIView subclass that displays ads capable of responding to user touch.
 */
@property(nonatomic, weak) IBOutlet GADBannerView *bannerView;

/**
 * @property
 * A UIView subclass that displays ads capable of responding to user touch.
 */
@property(nonatomic, strong) GADInterstitial *interstitial;

@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  self.bannerView.adUnitID = [GGLContext sharedInstance].adUnitIDForBannerTest;
  self.bannerView.rootViewController = self;
  [self.bannerView loadRequest:[GADRequest request]];
  // [END gmp_banner_example]

  // [START gmp_interstitial_example]
  self.interstitial = [self createAndLoadInterstitial];
}

- (GADInterstitial *)createAndLoadInterstitial {
  GADInterstitial *interstitial = [[GADInterstitial alloc]
      initWithAdUnitID:[GGLContext sharedInstance].adUnitIDForInterstitialTest];
  interstitial.delegate = self;
  [interstitial loadRequest:[GADRequest request]];
  return interstitial;
}

- (void)interstitialDidDismissScreen:(GADInterstitial *)interstitial {
  self.interstitial = [self createAndLoadInterstitial];
}

- (IBAction)didTapInterstitialButton:(id)sender {
  if ([self.interstitial isReady]) {
    [self.interstitial presentFromRootViewController:self];
  }
}

@end
// [END gmp_interstitial_example]
