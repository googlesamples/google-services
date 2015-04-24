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

class ViewController: UIViewController {

  var connectingToGCM = false;
  var apnsDeviceToken : NSData?
  // TODO(silvano): move to info.plist
  let senderId = "1052345580647"
  // TODO(silvano): replace with prod one
  let GCMSendUrl = "https://jmt17.google.com/gcm/send"

  @IBOutlet weak var activityIndicatorView:UIActivityIndicatorView!

  @IBOutlet weak var registerButtonView: UIButton!

  @IBOutlet weak var successLabelView: UILabel!

  override func viewDidLoad() {
    super.viewDidLoad()
    activityIndicatorView.hidesWhenStopped = true
    activityIndicatorView.startAnimating()
  }

  @IBAction func didTapRegister(sender: UIButton) {

    var types: UIUserNotificationType = UIUserNotificationType.Badge |
      UIUserNotificationType.Alert |
      UIUserNotificationType.Sound

    var settings: UIUserNotificationSettings = UIUserNotificationSettings( forTypes: types, categories: nil )

    let application = UIApplication.sharedApplication()
    application.registerUserNotificationSettings( settings )
    let instanceId = GMPInstanceID.sharedInstance()

    if (!connectingToGCM) {

      let appDelegate = application.delegate! as AppDelegate
      var options = [kGMPInstanceIDRegisterAPNSOption:apnsDeviceToken!,kGMPInstanceIDAPNSServerTypeSandboxOption: true]
      instanceId.tokenWithAudience(senderId, scope: kGMPInstanceIDScopeGCM, options: options, handler: {
        (registrationId: String!, error: NSError!) -> Void in

        // TODO(silvano): add a text boxt to allow the user to provide the api key?
        if (registrationId != nil) {
          println("Use this cURL command to send messages to your device," +
                  " replacing <YOUR_API_KEY> with the project number of" +
                  " your GCM enabled Google Developer Console project.\n\n")
          println(self.getCurlCommandForRequest(registrationId)!)
          self.registerButtonView.hidden = true
          let message = "Check the xcode debug console for a cURL command that you can use to send" +
              " a notification to your device"
          let success = UIAlertView(title: "Registration Successful!", message: message,
              delegate: self, cancelButtonTitle: "Dismiss")
          success.show()
        } else {
          println("Registration to GCM failed with error: \(error)")
        }

      })

      connectingToGCM = false

    }
  }

  func getCurlCommandForRequest(registrationId: String) -> String? {
    let deviceName = UIDevice.currentDevice().name
    let message = ["to": registrationId, "notification": ["alert": "Hello \(deviceName) from GCM"]]
    var jsonError:NSError? = nil
    let jsonBody = NSJSONSerialization.dataWithJSONObject(message, options: nil, error: &jsonError)
    if (jsonError == nil) {
      let payload = NSString(data: jsonBody!, encoding: NSUTF8StringEncoding)
      let escapedPayload = payload?.stringByReplacingOccurrencesOfString("\"", withString: "\\\"")
      let command = "curl --header \"Authorization: key=<YOUR_API_KEY>\"" +
                    " --header Content-Type:\"application/json\" \(GCMSendUrl) -d \"\(escapedPayload!)\""
      return command
    } else {
      println("Error encoding JSON: \(jsonError)")
      return nil
    }
  }

  func didReceiveAPNSToken(apnsDeviceToken: NSData!) {
    self.apnsDeviceToken = apnsDeviceToken
    enableRegistering()
  }

  func enableRegistering() {
    activityIndicatorView.stopAnimating()
    registerButtonView.hidden = false
  }

}
