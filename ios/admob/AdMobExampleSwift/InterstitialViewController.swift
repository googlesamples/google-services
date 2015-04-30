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
//  InterstitialViewController.swift
//  GoogleMobileAdsSample

// Makes ViewController available to Objc classes.
@objc(InterstitialViewController)
class InterstitialViewController: UIViewController, GADInterstitialDelegate {
    @IBOutlet weak var interstitialView: GADInterstitial!

    override func viewDidLoad() {
        super.viewDidLoad()

        self.interstitialView = GHIContext.sharedInstance().interstitialView
        self.interstitialView.delegate = self
        self.interstitialView.loadRequest(GADRequest())
    }

    func interstitialDidReceiveAd(interstitial: GADInterstitial!) {
        if (interstitial.isReady){
            interstitial.presentFromRootViewController(self)
        }
    }
}
