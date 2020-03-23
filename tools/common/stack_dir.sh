#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at https://mozilla.org/MPL/2.0/.

function push_dir() {
  dir=$1
  pushd $dir > /dev/null
  return $?
}

function pop_dir() {
  popd > /dev/null
  return $?
}
