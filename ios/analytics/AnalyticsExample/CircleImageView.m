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

#import "CircleImageView.h"

@implementation CircleImageView

- (instancetype)initWithFrame:(CGRect)frame {
  if ((self = [super initWithFrame:frame])) {
    [self sharedInit];
  }
  return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
  if ((self = [super initWithCoder:aDecoder])) {
    [self sharedInit];
  }
  return self;
}

- (void)sharedInit {
  self.backgroundColor = [UIColor whiteColor];
  super.contentMode = UIViewContentModeCenter;

  CALayer *layer = super.layer;
  layer.shadowOffset = CGSizeMake(0, 2);
  layer.shadowOpacity = 0.25;
  layer.shadowColor = [UIColor grayColor].CGColor;
  layer.shadowRadius = 4.0;
  layer.shadowOffset = CGSizeMake(0, 2);
}

- (void)layoutSubviews {
  [super layoutSubviews];

  CGSize size = self.bounds.size;
  CGFloat dim = MAX(size.width, size.height);
  self.layer.cornerRadius = dim / 2;
}

- (void)setContentMode:(UIViewContentMode)contentMode {
  // ignore
}

@end
