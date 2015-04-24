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

@objc(ViewController)
class ViewController: UIViewController {

  @IBOutlet var background: UIImageView?
  private var color: UIColor?

  override func viewWillAppear(animated: Bool) {
    super.viewWillAppear(true)

    var name = "Pattern~\(self.title!)"

    var tracker = GAI.sharedInstance().defaultTracker
    tracker.set(kGAIScreenName, value: name)

    var builder = GAIDictionaryBuilder.createScreenView()
    tracker.send(builder.build() as [NSObject : AnyObject])
  }

  override func viewDidAppear(animated: Bool) {
    super.viewDidAppear(animated)

    var image = background?.image
    var color: UIColor
    if image != nil {
      color = UIColor(patternImage:image!)
    } else {
      color = UIColor()
    }

    self.view.backgroundColor = color
  }

}
