#!/usr/bin/env bash

set -o pipefail && xcodebuild \
  -workspace ${SAMPLE}/${SAMPLE}Example.xcworkspace \
  -scheme ${SAMPLE}Example \
  -sdk iphonesimulator \
  -destination 'platform=iOS Simulator,name=iPhone 7' \
  build \
  test \
  ONLY_ACTIVE_ARCH=YES \
  CODE_SIGNING_REQUIRED=NO\
  | xcpretty
  
RESULT=$?
if [ $RESULT == 65 ]; then
  echo "xcodebuild exited with 65, retrying"
  set -o pipefail && xcodebuild \
    -workspace ${SAMPLE}/${SAMPLE}Example.xcworkspace \
    -scheme ${SAMPLE}Example \
    -sdk iphonesimulator \
    -destination 'platform=iOS Simulator,name=iPhone 7' \
    build \
    test \
    ONLY_ACTIVE_ARCH=YES \
    CODE_SIGNING_REQUIRED=NO\
    | xcpretty
else
  exit $RESULT
fi
