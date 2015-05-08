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
//  BannerExample
//

// [START gmp_banner_example]

#import "ViewController.h"
#import <Google/AdMob.h>

@interface ViewController ()

/**
 * @property
 * A UIView subclass that displays ads capable of responding to user touch.
 */
@property(nonatomic, strong) GADBannerView *bannerView;
@property (weak, nonatomic) IBOutlet UIButton *interstitialButton;

@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  self.bannerView = [GGLContext sharedInstance].bannerView;
  [self.bannerView setAdSize:kGADAdSizeSmartBannerPortrait];
  self.bannerView.frame = CGRectMake(0,
                                     self.view.frame.size.height -
                                     self.bannerView.frame.size.height,
                                     self.bannerView.frame.size.width,
                                     self.bannerView.frame.size.height);
  self.bannerView.rootViewController = self;
  [self.view addSubview:self.bannerView];
  [self.bannerView loadRequest:[GADRequest request]];
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    [self.interstitialButton setEnabled:NO];
    NSLog(@"GADInterstitial is a one time use object.");
}
@end
// [END gmp_banner_example]
