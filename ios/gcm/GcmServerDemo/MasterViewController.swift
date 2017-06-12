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

  let sendUrl = "https://android.googleapis.com/gcm/send"
  let subscriptionTopic = "/topics/global"

  @IBOutlet weak var apiKeyTextField: NSTextField!
  @IBOutlet weak var regIdTextField: NSTextField!
  @IBOutlet weak var sendNotificationButton: NSButton!
  @IBOutlet weak var displayCurlButton: NSButton!
  @IBOutlet weak var curlCommandTextView: NSScrollView!
  @IBOutlet weak var sendToTopicButton: NSButton!

  override func controlTextDidChange(obj: NSNotification) {
    // Length checks just to ensure that the text fields don't contain just a couple of chars
    if (apiKeyTextField.stringValue.utf16.count) >= 30 {
      sendToTopicButton.enabled = true
      if (regIdTextField.stringValue.utf16.count) >= 30 {
        sendNotificationButton.enabled = true
        displayCurlButton.enabled = true
      } else {
        sendNotificationButton.enabled = false
        displayCurlButton.enabled = false
      }
    } else {
      sendToTopicButton.enabled = false
    }
  }

  @IBAction func didClickSendNotification(sender: NSButton) {
    sendMessage(getRegToken())
  }

  @IBAction func didClickDisplaycURL(sender: NSButton) {
    let message = getMessage(getRegToken())
    var jsonBody: NSData
    do {
      jsonBody = try NSJSONSerialization.dataWithJSONObject(message, options: [])
      let payload = NSString(data: jsonBody, encoding: NSUTF8StringEncoding)
      let escapedPayload = payload?.stringByReplacingOccurrencesOfString("\"", withString: "\\\"")
      let command = "curl --header \"Authorization: key=\(getApiKey())\"" +
      " --header Content-Type:\"application/json\" \(sendUrl) -d \"\(escapedPayload!)\""
      curlCommandTextView.documentView!.setString(command)
    } catch let error as NSError {
      NSAlert(error: error).runModal()
    }
  }

  @IBAction func didClickSendToTopic(sender: NSButton) {
    sendMessage(subscriptionTopic)
  }

  func sendMessage(to: String) {
    // Create the request.
    let request = NSMutableURLRequest(URL: NSURL(string: sendUrl)!)
    request.HTTPMethod = "POST"
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    request.setValue("key=\(getApiKey())", forHTTPHeaderField: "Authorization")
    request.timeoutInterval = 60

    // prepare the payload
    let message = getMessage(to)
    let jsonBody: NSData?
    do {
      jsonBody = try NSJSONSerialization.dataWithJSONObject(message, options: [])
      request.HTTPBody = jsonBody!
      NSURLConnection.sendAsynchronousRequest(request, queue: NSOperationQueue.mainQueue(),
        completionHandler: { (response: NSURLResponse?, data: NSData?, error: NSError?) -> Void in
          if error != nil {
            NSAlert(error: error!).runModal()
          } else {
            print("Success! Response from the GCM server:")
            print(response)
          }
      })
    } catch let error as NSError {
      NSAlert(error: error).runModal()
    }

  }

  func getMessage(to: String) -> NSDictionary {
  // [START notification_format]
      return ["to": to, "notification": ["body": "Hello from GCM"]]
  // [END notification_format]
  }

  func getApiKey() -> String {
    return apiKeyTextField.stringValue.stringByReplacingOccurrencesOfString("\n", withString: "")
  }

  func getRegToken() -> String {
    return regIdTextField.stringValue.stringByReplacingOccurrencesOfString("\n", withString: "")
  }

}
