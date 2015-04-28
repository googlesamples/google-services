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

import AppKit
import Cocoa

class MasterViewController: NSViewController, NSTextFieldDelegate {

  // TODO(silvano): replace with prod URL when available
  let sendUrl = "https://jmt17.google.com/gcm/send"

  @IBOutlet weak var apiKeyTextField: NSTextField!
  @IBOutlet weak var regIdTextField: NSTextField!
  @IBOutlet weak var sendNotificationButton: NSButton!
  @IBOutlet weak var displayCurlButton: NSButton!
  @IBOutlet weak var curlCommandTextView: NSScrollView!

  override func controlTextDidChange(obj: NSNotification) {
    // Length checks just to ensure that the text fields don't contain just a couple of chars
    if apiKeyTextField.stringValue.utf16Count >= 30 &&
        regIdTextField.stringValue.utf16Count >= 30{
      sendNotificationButton.enabled = true
      displayCurlButton.enabled = true
    } else {
      sendNotificationButton.enabled = false
      displayCurlButton.enabled = false
    }
  }

  @IBAction func didClickSendNotification(sender: NSButton) {
    // Create the request.
    var request = NSMutableURLRequest(URL: NSURL(string: sendUrl)!)
    request.HTTPMethod = "POST"
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    request.setValue("key=\(apiKeyTextField.stringValue)", forHTTPHeaderField: "Authorization")
    request.timeoutInterval = 60

    // prepare the payload
    let message = getMessage()
    var jsonError:NSError?
    let jsonBody = NSJSONSerialization.dataWithJSONObject(message, options: nil, error: &jsonError)
    if (jsonError == nil) {
      request.HTTPBody = jsonBody
      NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue.mainQueue(), completionHandler: {
          (response: NSURLResponse!, data: NSData!, error: NSError!) -> Void in
        if error != nil {
          NSAlert(error: error).runModal()
        } else {
          println("Success! Response from the GCM server:")
          println(response)
        }
      })
    } else {
      NSAlert(error: jsonError!).runModal()
    }
  }

  @IBAction func didClickDisplaycURL(sender: NSButton) {
    let message = getMessage()
    var jsonError:NSError?
    let jsonBody = NSJSONSerialization.dataWithJSONObject(message, options: nil, error: &jsonError)
    if (jsonError == nil) {
      let payload = NSString(data: jsonBody!, encoding: NSUTF8StringEncoding)
      let escapedPayload = payload?.stringByReplacingOccurrencesOfString("\"", withString: "\\\"")
      let command = "curl --header \"Authorization: key=\(apiKeyTextField.stringValue)\"" +
      " --header Content-Type:\"application/json\" \(sendUrl) -d \"\(escapedPayload!)\""
      curlCommandTextView.documentView!.setString(command)
    } else {
      NSAlert(error: jsonError!).runModal()
    }
  }

  func getMessage() -> NSDictionary {
    return ["to": regIdTextField.stringValue, "notification": ["body": "Hello from GCM"]]
  }

}
