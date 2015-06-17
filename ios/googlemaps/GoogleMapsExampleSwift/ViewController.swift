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

import UIKit
import GoogleMaps

@objc(ViewController)
class ViewController: UIViewController, GMSMapViewDelegate {

  @IBOutlet var mapView: GMSMapView!

  override func viewDidLoad() {
    super.viewDidLoad()

    let sydney = CLLocationCoordinate2DMake(-33.8650, 151.2094)

    mapView.camera = GMSCameraPosition.cameraWithTarget(sydney, zoom: 14)
    mapView.delegate = self

    let marker = GMSMarker(position: sydney)
    marker.title = "Sydney"
    marker.map = mapView
  }

  // MARK: - GMSMapViewDelegate

  func mapView(mapView: GMSMapView!, idleAtCameraPosition position: GMSCameraPosition!) {
    println("MapView idle at (\(position.target.latitude), \(position.target.longitude))")
  }

}
