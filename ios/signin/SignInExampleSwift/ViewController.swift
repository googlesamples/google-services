//
//  Copyright (c) Google Inc.
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

// Match the ObjC symbol name inside Main.storyboard.
@objc(ViewController)
// [START viewcontroller_interfaces]
class ViewController: UIViewController, GIDSignInUIDelegate {
// [END viewcontroller_interfaces]

  // [START viewcontroller_vars]
  @IBOutlet weak var signInButton: GIDSignInButton!
  @IBOutlet weak var signOutButton: UIButton!
  @IBOutlet weak var disconnectButton: UIButton!
  @IBOutlet weak var statusText: UILabel!
  // [END viewcontroller_vars]

  // [START viewdidload]
  override func viewDidLoad() {
    super.viewDidLoad()

    GIDSignIn.sharedInstance().uiDelegate = self

    // Uncomment to automatically sign in the user.
    //GIDSignIn.sharedInstance().signInSilently()

    // TODO(developer) Configure the sign-in button look/feel
    // [START_EXCLUDE]
    NSNotificationCenter.defaultCenter().addObserver(self,
        selector: #selector(ViewController.receiveToggleAuthUINotification(_:)),
        name: "ToggleAuthUINotification",
        object: nil)

    statusText.text = "Initialized Swift app..."
    toggleAuthUI()
    // [END_EXCLUDE]
  }
  // [END viewdidload]

  // [START signout_tapped]
  @IBAction func didTapSignOut(sender: AnyObject) {
    GIDSignIn.sharedInstance().signOut()
    // [START_EXCLUDE silent]
    statusText.text = "Signed out."
    toggleAuthUI()
    // [END_EXCLUDE]
  }
  // [END signout_tapped]

  // [START disconnect_tapped]
  @IBAction func didTapDisconnect(sender: AnyObject) {
    GIDSignIn.sharedInstance().disconnect()
    // [START_EXCLUDE silent]
    statusText.text = "Disconnecting."
    // [END_EXCLUDE]
  }
  // [END disconnect_tapped]

  // [START toggle_auth]
  func toggleAuthUI() {
    if (GIDSignIn.sharedInstance().hasAuthInKeychain()){
      // Signed in
      signInButton.hidden = true
      signOutButton.hidden = false
      disconnectButton.hidden = false
    } else {
      signInButton.hidden = false
      signOutButton.hidden = true
      disconnectButton.hidden = true
      statusText.text = "Google Sign in\niOS Demo"
    }
  }
  // [END toggle_auth]

  override func preferredStatusBarStyle() -> UIStatusBarStyle {
    return UIStatusBarStyle.LightContent
  }

  deinit {
    NSNotificationCenter.defaultCenter().removeObserver(self,
        name: "ToggleAuthUINotification",
        object: nil)
  }

  @objc func receiveToggleAuthUINotification(notification: NSNotification) {
    if (notification.name == "ToggleAuthUINotification") {
      self.toggleAuthUI()
      if notification.userInfo != nil {
        let userInfo:Dictionary<String,String!> =
            notification.userInfo as! Dictionary<String,String!>
        self.statusText.text = userInfo["statusText"]
      }
    }
  }

}
