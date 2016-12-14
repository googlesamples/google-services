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

#import "LogWrapper.h"

#include <stdlib.h>
#include <stdio.h>

#define ARRAYSIZE(x) (sizeof(x)/sizeof(x[0]))

@implementation LogWrapper {
  char _tempLogFile[64];
  NSFileHandle *_handle;
  int _dupFd;
}

- (instancetype)init {
  if ((self = [super init])) {
    _dupFd = dup(fileno(stderr));

    snprintf(_tempLogFile, ARRAYSIZE(_tempLogFile), "/tmp/objc-logwrapper.XXXXXX");
    int fd = mkstemp(_tempLogFile);

    NSLog(@"LogWrapper writing to: %s", _tempLogFile);

    freopen(_tempLogFile, "w+", stderr);
    _handle = [[NSFileHandle alloc] initWithFileDescriptor:fd closeOnDealloc:YES];
  }
  return self;
}

- (void)dealloc {
  unlink(_tempLogFile);
  NSLog(@"~LogWrapper removed: %s", _tempLogFile);

  dup2(_dupFd, fileno(stderr));
  close(_dupFd);
}

- (NSArray *)lines {
  NSData *data = [_handle readDataToEndOfFile];
  [_handle seekToFileOffset:data.length];

  if (data == nil || !data.bytes) {
    return nil;
  }

  NSString *allData = @((const char *)data.bytes);
  return [allData componentsSeparatedByString:@"\n"];
}

@end
