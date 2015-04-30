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
//  appinvites

import UIKit

// Match the ObjC symbol name inside Main.storyboard.
@objc(ViewController)
// [START viewcontroller_interfaces]
class ViewController: UIViewController, GIDSignInDelegate, GINInviteDelegate {
// [END viewcontroller_interfaces]
  // [START viewcontroller_vars]
  @IBOutlet weak var signInButton: GIDSignInButton!
  @IBOutlet weak var signOutButton: UIButton!
  @IBOutlet weak var disconnectButton: UIButton!
  @IBOutlet weak var inviteButton: UIButton!
  @IBOutlet weak var statusText: UILabel!
  // [END viewcontroller_vars]

  // [START viewdidload]
  override func viewDidLoad() {
    super.viewDidLoad()
    GIDSignIn.sharedInstance().delegate = self
    statusText.text = "Initialized Swift demo"
    toggleAuthUI()
  }
  // [END viewdidload]

  // [START signin_handler]
  func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!, withError error: NSError!) {
    if (error == nil) {
      // User Successfully signed in.
      statusText.text = "Signed in ID: \(user.userID)"
      toggleAuthUI()
    } else {
      println("\(error.localizedDescription)")
      toggleAuthUI()
    }
  }
  // [END signin_handler]

  // [START signout_tapped]
  @IBAction func signOutTapped(sender: AnyObject) {
    GIDSignIn.sharedInstance().signOut()
    statusText.text = "Signed out"
    toggleAuthUI()
  }
  // [END signout_tapped]

  // [START disconnect_tapped]
  @IBAction func disconnectTapped(sender: AnyObject) {
    GIDSignIn.sharedInstance().disconnect()
    statusText.text = "Disconnected"
    toggleAuthUI()
  }
  // [END disconnect_tapped]

  // [START invite_tapped]
  @IBAction func inviteTapped(sender: AnyObject) {
    let invite = GINInvite.inviteDialog()
    invite.setMessage("Message")
    invite.setTitle("Title")
    invite.setDeepLink("/invite")
    invite.setEmailMessage("Email message")

    invite.open()
  }
  // [END invite_tapped]

  // [START toggle_auth]
  func toggleAuthUI() {
    if (GIDSignIn.sharedInstance().hasAuthInKeychain()){
      // Signed in
      signInButton.enabled = false
      signOutButton.enabled = true
      disconnectButton.enabled = true
      inviteButton.enabled = true
    } else {
      signInButton.enabled = true
      signOutButton.enabled = false
      disconnectButton.enabled = false
      inviteButton.enabled = false
    }
  }
  // [END toggle_auth]

  // [START invite_finished]
  func inviteFinishedWithInvitations(invitationIds: [AnyObject]!, error: NSError!) {
    if (error != nil){
      println("Failed: " + error.localizedDescription)
    }else{
      println("Invitations sent")
    }
  }
  // [END invite_finished]
}
