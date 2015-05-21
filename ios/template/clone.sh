#
# Copyright Google Inc. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#!/bin/bash

# Clones the GMPExample project into a new example.

set -eu
cd ${0%/*}  # chdir to script path

print_usage() {
  local name=$(basename "${0}")
  cat << EOF
usage: ${name} -e EXAMPLE [-o PATH] [-p POD]

Clones the $SOURCE project to a new, local example.

OPTIONS:
   -h, --help
         Show this message.
   -e, --example EXAMPLE
         Specify the example name, e.g. "FooExample".
   -p, --pod POD
         Specify the pod to include, e.g. "GoogleAnalytics-iOS-SDK". Leave
         empty or blank for none -- you can update the Podfile later.
   -o, --out PATH
         Specify the path to use, e.g. "foo". If this is unspecified, the left
         part of "BarExample" will be used, aka "bar".

EOF
}

SOURCE="GMPExample"
DEST=
OUT=
POD=
while [[ $# != 0 ]]; do
  case "${1}" in
    -e | --example )
      shift
      DEST="${1}"
      ;;
    -o | --out )
      shift
      OUT="${1}"
      ;;
    -p | --pod )
      shift
      POD="${1}"
      ;;
    -h | --help )
      print_usage
      exit 0
      ;;
    *) echo "unknown: $1"; exit 1 ;;
  esac
  shift
done

# Check args, and convert DEST of "FooExample" to OUT of "foo" if unspecified.
if [[ -z "$DEST" ]]; then
  print_usage 1>&2
  exit 1
fi
if [[ -z "$OUT" ]]; then
  if [[ "$DEST" =~ (.+)Example ]]; then
    OUT="${BASH_REMATCH[1]}"
    OUT=$(echo $OUT | tr "[:upper:]" "[:lower:]")
  fi
  if [[ -z "$OUT" ]]; then
    echo "Couldn't determine path from: ${DEST}, specify with -o." 1>&2
    exit 1
  fi
fi

T=$PWD
OUT_PATH="../${OUT}"

if [ -d "${OUT_PATH}" ]; then
  echo "Output path already exists: ${OUT}" 1>&2
  exit 2
fi

# >> Part 1: Copy GMPExample* to the new sample folder.
echo "Copying to: ${OUT}"
SUFFIXES=("" "Swift" "Tests" ".xcodeproj/project.pbxproj")
for SUFFIX in "${SUFFIXES[@]}"; do
  TARGET="${OUT_PATH}/${DEST}${SUFFIX}"
  DIR=$(dirname "${TARGET}")
  mkdir -p $DIR
  echo "  ${SOURCE}${SUFFIX} => ${TARGET}"
  cp -R $T/"${SOURCE}${SUFFIX}" "${TARGET}"
done
echo

# >> Part 2: Copy misc license/etc files to the new sample folder.
cp -Rv $T/layout/* ${OUT_PATH}

# >> Part 3: Update text content from GMPExample => YourExample.
cd ${OUT_PATH}

# Fix the pbxproj file.
PROJECT="${DEST}.xcodeproj/project.pbxproj"
echo "Replacing $SOURCE with $DEST in: $PROJECT"
cat $PROJECT | sed s/$SOURCE/$DEST/g > tmp
mv tmp $PROJECT

# Check that GMPExample does not exist in the new folder.
grep $SOURCE -R . && {
  echo "Found GMPExample in new folder: ${OUT}, fix manually"
  exit 1
}

# Create a stock Podfile.
echo "Creating Podfile..."
echo "# $DEST" > Podfile
echo "platform :ios, '7.0'" >> Podfile
if [[ -n "${POD}" ]]; then
  echo "Including pod: $POD"
  echo "pod '$POD'" >> Podfile
fi
echo "link_with '${DEST}', '${DEST}Swift', '${DEST}Tests'" >> Podfile

# Setup pods. This fetches the pods themselves, but they should be ignored by
# version control anyway via the master .gitignore.
pod install

echo "Done!"
