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

    @IBOutlet var deepLinkLabel: UILabel!
    
    // [START display_link]
    override func viewDidLoad() {
        super.viewDidLoad()
        
        NSNotificationCenter.defaultCenter().addObserver(self,
            selector: "applicationBecameActive:",
            name: UIApplicationDidBecomeActiveNotification,
            object: nil)
    }
    
    func applicationBecameActive(notification: NSNotification){
        let appDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        deepLinkLabel.text = appDelegate.currentDeepLink
    }
    // [END display_link]

}
