/*
 *
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//  InterstitialViewController.m
//  GoogleMobileAdsSample

#import "InterstitialViewController.h"

#import <Google/AdMob.h>

@interface InterstitialViewController () <GADInterstitialDelegate>

/**
 * @property
 * A UIView subclass that displays ads capable of responding to user touch.
 */
@property(nonatomic, strong) GADInterstitial *interstitialView;

@end

@implementation InterstitialViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  self.interstitialView = [GGLContext sharedInstance].interstitialView;
  self.interstitialView.delegate = self;
  [self.interstitialView loadRequest:[GADRequest request]];
}

- (GADInterstitial *)createAndLoadInterstitial {
  GADInterstitial *interstitial = [[GADInterstitial alloc] init];
  interstitial.adUnitID = [GGLContext sharedInstance].adUnitIDForInterstitialTest;
  interstitial.delegate = self;
  [interstitial loadRequest:[GADRequest request]];
  return interstitial;
}

- (void)interstitialDidDismissScreen:(GADInterstitial *)interstitial {
//  self.interstitialView = [self createAndLoadInterstitial];
}

- (void)interstitialDidReceiveAd:(GADInterstitial *)interstitial {
  if ([interstitial isReady]){
    [interstitial presentFromRootViewController:self];
  }
}

@end
