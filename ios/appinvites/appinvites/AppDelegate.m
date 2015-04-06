//
//  AppDelegate.m
//  appinvites
//
//  Created by Gus Class on 4/3/15.
//  Copyright (c) 2015 gusclass. All rights reserved.
//

#import "AppDelegate.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
  [GIDSignIn sharedInstance].clientID = @"YOUR_CLIENT_ID.apps.googleusercontent.com";
  [GIDSignIn sharedInstance].scopes = @[ @"https://www.googleapis.com/auth/plus.login" ];

  return YES;
}


- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
  return [[GIDSignIn sharedInstance] handleURL:url
                             sourceApplication:sourceApplication
                                    annotation:annotation];
}

@end
