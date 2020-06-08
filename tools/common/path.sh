#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at https://mozilla.org/MPL/2.0/.

function triple_banana_tools_path() {
  echo $TOOLS_PATH
}

function triple_banana_path() {
  echo $(realpath $(triple_banana_tools_path)/..)
}

function triple_banana_build_gn_path() {
  echo $(triple_banana_path)/build/gn
}

function triple_banana_command_path() {
  echo $(triple_banana_tools_path)/command
}

function triple_banana_upstream_path() {
  echo $(triple_banana_path)/upstream
}

function triple_banana_resource_path() {
  echo $(triple_banana_path)/res
}

function triple_banana_private_path() {
  echo $(chromium_path)/triple_banana_private
}

function override_resource_path() {
  echo $(triple_banana_path)/res_override
}

function chromium_path() {
  echo $(realpath $(triple_banana_path)/..)
}

function chromium_out_release_path() {
  echo $(chromium_path)/out/release
}
