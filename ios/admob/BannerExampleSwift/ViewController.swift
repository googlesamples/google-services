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
//  ViewController.swift
//  BannerExample
//

import UIKit

// Makes ViewController available to Objc classes.
@objc(ViewController)
class ViewController: UIViewController {
    @IBOutlet weak var bannerView: GADBannerView!

    override func viewDidLoad() {
        super.viewDidLoad()

        // Replace this ad unit ID with your own ad unit ID.
        self.bannerView.adUnitID = GHIContext.sharedInstance().defaultAdUnitID
        self.bannerView.rootViewController = self

        let request = GADRequest();
        // Requests test ads on devices you specify. Your test device ID is printed to the console when
        // an ad request is made. GADBannerView automatically returns test ads when running on a
        // simulator.
        // Replace this device ID with your own test device ID.
        request.testDevices = [
        "2077ef9a63d2b398840261c8221a0c9a"
        ]
        self.bannerView.loadRequest(request)
    }

}

