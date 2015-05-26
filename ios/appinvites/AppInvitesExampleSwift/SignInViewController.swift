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
@objc(SignInViewController)

class SignInViewController: UIViewController, GIDSignInDelegate, GINInviteDelegate, GIDSignInUIDelegate {

  @IBOutlet weak var signInButton: GIDSignInButton!
  @IBOutlet weak var bgText: UILabel!

  override func viewDidAppear(animated: Bool) {
    super.viewDidAppear(animated)

    bgText.text = "App Invites\niOS demo"
    GIDSignIn.sharedInstance().delegate = self
    GIDSignIn.sharedInstance().uiDelegate = self
    GIDSignIn.sharedInstance().signInSilently()
  }

  func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!, withError error: NSError!) {
    if (error == nil) {
      // User Successfully signed in.
      self.performSegueWithIdentifier("SignedInScreen", sender: self)
    } else {
      // Something went wrong; for example, the user could haved clicked cancel.
    }
  }

  @IBAction func unwindToSignIn(sender: UIStoryboardSegue) {
    GIDSignIn.sharedInstance().delegate = self
  }

  // Sets the status bar to white.
  override func preferredStatusBarStyle() -> UIStatusBarStyle {
    return UIStatusBarStyle.LightContent
  }
}
