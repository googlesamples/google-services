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

@objc(ViewController)  // match the ObjC symbol name inside Storyboard
class ViewController: UIViewController {

  @IBOutlet weak var registeringLabel: UILabel!
  @IBOutlet weak var registrationProgressing: UIActivityIndicatorView!

  override func viewDidLoad() {
    super.viewDidLoad()
    let appDelegate = UIApplication.sharedApplication().delegate as AppDelegate
    NSNotificationCenter.defaultCenter().addObserver(self, selector: "updateRegistrationStatus:",
        name: appDelegate.notificationKey, object: nil)
    registrationProgressing.hidesWhenStopped = true
    registrationProgressing.startAnimating()
  }

  func updateRegistrationStatus(notification: NSNotification) {
    registrationProgressing.stopAnimating()
    if let info = notification.userInfo as? Dictionary<String,String> {
      if let error = info["error"] {
        registeringLabel.text = "Error registering!"
        let alert = UIAlertController(title: "Error registering with GCM", message: error,
            preferredStyle: .Alert)
        let dismissAction = UIAlertAction(title: "Dismiss", style: .Destructive, handler: nil)
        alert.addAction(dismissAction)
        self.presentViewController(alert, animated: true, completion: nil)
      } else if let registrationToken = info["registrationToken"] {
        registeringLabel.text = "Registered!"
        let message = "Check the xcode debug console for the registration token that you " +
        " can use with the demo server to send notifications to your device"
        let success = UIAlertController(title: "Registration Successful!",
          message: message, preferredStyle: .Alert)
        let dismissAction = UIAlertAction(title: "Dismiss", style: .Destructive, handler: nil)
        success.addAction(dismissAction)
        self.presentViewController(success, animated: true, completion: nil)
      }
    } else {
      println("Software failure. Guru meditation.")
    }
  }

  deinit {
    NSNotificationCenter.defaultCenter().removeObserver(self)
  }
}
