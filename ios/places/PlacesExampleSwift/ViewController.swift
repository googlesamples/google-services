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
class ViewController: UIViewController, UITextViewDelegate {

  var placePicker: GMSPlacePicker?

  @IBOutlet weak var placeName: UILabel!
  @IBOutlet weak var placeAddress: UILabel!
  @IBOutlet weak var placeAttribution: UITextView!

  override func viewDidLoad() {
    super.viewDidLoad()
    placeAttribution.delegate = self
  }

  @IBAction func pickPlace(sender: AnyObject) {
    let center = CLLocationCoordinate2DMake(51.5108396, -0.0922251)
    let northEast = CLLocationCoordinate2DMake(center.latitude + 0.001,
      center.longitude + 0.001)
    let southWest = CLLocationCoordinate2DMake(center.latitude - 0.001,
      center.longitude - 0.001)
    let viewport = GMSCoordinateBounds(coordinate: northEast, coordinate: southWest)
    let config = GMSPlacePickerConfig(viewport: viewport)
    placePicker = GMSPlacePicker(config: config)

    placePicker?.pickPlaceWithCallback({ (place: GMSPlace?, error: NSError?) -> Void in
      self.placeName.text = ""
      self.placeAddress.text = ""
      self.placeAttribution.text = ""

      if let error = error {
        println("Pick Place error \(error.localizedDescription)")
        return
      }

      if let place = place {
        self.placeName.text = place.name
        self.placeAddress.text = "\n".join(place.formattedAddress.componentsSeparatedByString(", "))
        self.placeAttribution.attributedText = place.attributions
      } else {
        self.placeName.text = "No place selected"
      }
    })
  }

  // MARK: - UITextViewDelegate

  func textView(textView: UITextView, shouldInteractWithURL URL: NSURL,
    inRange characterRange: NSRange) -> Bool {
      // Make Places API Attribution links clickable.
      return true
  }
}
