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
@import GoogleMaps;

@interface ViewController () <UITextViewDelegate>
@property (weak, nonatomic) IBOutlet UILabel *placeName;
@property (weak, nonatomic) IBOutlet UILabel *placeAddress;
@property (weak, nonatomic) IBOutlet UITextView *placeAttribution;
@end

@implementation ViewController {
  GMSPlacePicker *_placePicker;
}

-(void)viewDidLoad {
  [super viewDidLoad];
  self.placeAttribution.delegate = self;
}

- (IBAction)pickPlace:(id)sender {
  CLLocationCoordinate2D center = CLLocationCoordinate2DMake(51.5108396, -0.0922251);
  CLLocationCoordinate2D northEast = CLLocationCoordinate2DMake(center.latitude + 0.001,
                                                                center.longitude + 0.001);
  CLLocationCoordinate2D southWest = CLLocationCoordinate2DMake(center.latitude - 0.001,
                                                                center.longitude - 0.001);
  GMSCoordinateBounds *viewport = [[GMSCoordinateBounds alloc] initWithCoordinate:northEast
                                                                       coordinate:southWest];
  GMSPlacePickerConfig *config = [[GMSPlacePickerConfig alloc] initWithViewport:viewport];
  _placePicker = [[GMSPlacePicker alloc] initWithConfig:config];

  [_placePicker pickPlaceWithCallback:^(GMSPlace *place, NSError *error) {
    self.placeName.text = @"";
    self.placeAddress.text = @"";
    self.placeAttribution.text = @"";

    if (error != nil) {
      NSLog(@"Pick Place error %@", [error localizedDescription]);
      return;
    }

    if (place != nil) {
      self.placeName.text = place.name;
      self.placeAddress.text = [[place.formattedAddress componentsSeparatedByString:@", "]
                                componentsJoinedByString:@"\n"];
      self.placeAttribution.attributedText = place.attributions;
    } else {
      self.placeName.text = @"No place selected";
    }
  }];
}

#pragma mark - UITextViewDelegate

- (BOOL)textView:(UITextView *)textView
    shouldInteractWithURL:(NSURL *)url
         inRange:(NSRange)characterRange {
  // Make Places API Attribution links clickable.
  return YES;
}

@end
