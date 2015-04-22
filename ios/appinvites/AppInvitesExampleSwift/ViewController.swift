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

class ViewController: UIViewController, GIDSignInDelegate, GPPInviteDelegate {
  @IBOutlet weak var signInButton: GIDSignInButton!
  @IBOutlet weak var signOutButton: UIButton!
  @IBOutlet weak var disconnectButton: UIButton!
  @IBOutlet weak var inviteButton: UIButton!

  override func viewDidLoad() {
    super.viewDidLoad()
    GIDSignIn.sharedInstance().delegate = self
    updateUI()
  }

  func signIn(signIn: GIDSignIn!, didSignInForUser user: GIDGoogleUser!, withError error: NSError!) {
    if (error == nil) {
      // User Successfully signed in.
      updateUI()
    } else {
      println("\(error.localizedDescription)")
      updateUI()
    }
  }

  @IBAction func signOutTapped(sender: AnyObject) {
    GIDSignIn.sharedInstance().signOut()
    updateUI()
  }

  @IBAction func disconnectTapped(sender: AnyObject) {
    GIDSignIn.sharedInstance().disconnect()
    updateUI()
  }

  @IBAction func inviteTapped(sender: AnyObject) {
    let invite = GPPInvite.inviteDialog()
    invite.setMessage("Message")
    invite.setTitle("Title")
    invite.setDeepLink("/invite")
    invite.setEmailMessage("Email message")

    invite.open()
  }

  func updateUI() {
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

  func inviteFinishedWithInvitations(invitationIds: [AnyObject]!, error: NSError!) {
    if (error != nil){
      println("Failed: " + error.localizedDescription)
    }else{
      println("Invitations sent")
    }
  }
}
