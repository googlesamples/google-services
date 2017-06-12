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
    guard let appDelegate = UIApplication.shared.delegate as? AppDelegate else { return }
    NotificationCenter.default.addObserver(self,
                                                     selector: #selector(ViewController.updateRegistrationStatus(_:)),
                                                     name: NSNotification.Name(rawValue: appDelegate.registrationKey), object: nil)
    NotificationCenter.default.addObserver(self,
                                                     selector: #selector(ViewController.showReceivedMessage(_:)),
                                                     name: NSNotification.Name(rawValue: appDelegate.messageKey), object: nil)
    registrationProgressing.hidesWhenStopped = true
    registrationProgressing.startAnimating()
  }

  func updateRegistrationStatus(_ notification: Notification) {
    registrationProgressing.stopAnimating()
    if let info = notification.userInfo as? [String:String] {
      if let error = info["error"] {
        registeringLabel.text = "Error registering!"
        showAlert("Error registering with GCM", message: error)
      } else if let _ = info["registrationToken"] {
        registeringLabel.text = "Registered!"
        let message = "Check the xcode debug console for the registration token that you " +
        " can use with the demo server to send notifications to your device"
        showAlert("Registration Successful!", message: message)
      }
    } else {
      print("Software failure. Guru meditation.")
    }
  }

  func showReceivedMessage(_ notification: Notification) {
    if let info = notification.userInfo as? [String:AnyObject] {
      if let aps = info["aps"] as? [String:String] {
        showAlert("Message received", message: aps["alert"]!)
      }
    } else {
      print("Software failure. Guru meditation.")
    }
  }

  func showAlert(_ title: String, message: String) {
    if #available(iOS 8.0, *) {
      let alert = UIAlertController(title: title,
          message: message, preferredStyle: .alert)
      let dismissAction = UIAlertAction(title: "Dismiss", style: .destructive, handler: nil)
      alert.addAction(dismissAction)
      self.present(alert, animated: true, completion: nil)
    } else {
        // Fallback on earlier versions
      let alert = UIAlertView.init(title: title, message: message, delegate: nil,
          cancelButtonTitle: "Dismiss")
      alert.show()
    }
  }

  override var preferredStatusBarStyle: UIStatusBarStyle {
    return UIStatusBarStyle.lightContent
  }

  deinit {
    NotificationCenter.default.removeObserver(self)
  }
}
