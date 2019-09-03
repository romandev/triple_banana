#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# Use of this source code is governed by a BSD-style license that can be
# found in the LICENSE file.

function push_dir() {
  dir=$1
  pushd $dir > /dev/null
  return $?
}

function pop_dir() {
  popd > /dev/null
  return $?
}
