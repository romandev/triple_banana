#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# Use of this source code is governed by a BSD-style license that can be
# found in the LICENSE file.

. $TOOLS_PATH/common/path.sh
. $(triple_banana_path)/VERSION

function get_chromium_version() {
  echo $CHROMIUM_VERSION
}

function get_triple_banana_version() {
  local banana_version=$BANANA_VERSION
  local chromium_version=($(echo $CHROMIUM_VERSION | tr "." " "))

  local major=${banana_version[0]}
  local chromium_major=${chromium_version[0]}
  local chromium_build=${chromium_version[2]}
  local chromium_patch=${chromium_version[3]}

  echo "$major.$chromium_major.$chromium_build.$chromium_patch"
}

function write_version_file() {
  local banana_version=$BANANA_VERSION
  local chromium_version=($(echo $CHROMIUM_VERSION | tr "." " "))

  local major=${banana_version[0]}
  local chromium_major=${chromium_version[0]}
  local chromium_build=${chromium_version[2]}
  local chromium_patch=${chromium_version[3]}

  echo "MAJOR=$major" > $(chromium_path)/chrome/VERSION
  echo "MINOR=$chromium_major" >> $(chromium_path)/chrome/VERSION
  echo "BUILD=$chromium_build" >> $(chromium_path)/chrome/VERSION
  echo "PATCH=$chromium_patch" >> $(chromium_path)/chrome/VERSION
}
