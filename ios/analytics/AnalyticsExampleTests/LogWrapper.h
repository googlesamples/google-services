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

#import <Foundation/Foundation.h>

/**
 * LogWrapper replaces the current stderr, matched to its lifetime. Any lines written to stderr
 * during this time can be retrieved by the -lines method.
 */
@interface LogWrapper : NSObject

/**
 * Returns any lines (as NSString instances) written since LogWrapper was created, or the last
 * call to this method. This may return a nil/empty array if nothing was written.
 */
- (NSArray *)lines;

@end

