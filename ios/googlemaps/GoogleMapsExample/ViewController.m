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

@interface ViewController () <GMSMapViewDelegate>
@property (strong, nonatomic) IBOutlet GMSMapView *mapView;
@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  CLLocationCoordinate2D sydney = CLLocationCoordinate2DMake(-33.8650, 151.2094);

  self.mapView.camera = [GMSCameraPosition cameraWithTarget:sydney zoom:14];
  self.mapView.delegate = self;

  GMSMarker *marker = [GMSMarker markerWithPosition:sydney];
  marker.title = @"Sydney";
  marker.map = self.mapView;
}

#pragma MARK - GMSMapViewDelegate

- (void)mapView:(GMSMapView *)mapView idleAtCameraPosition:(GMSCameraPosition *)position {
  NSLog(@"MapView idle at (%g, %g)", position.target.latitude, position.target.longitude);
}

@end
