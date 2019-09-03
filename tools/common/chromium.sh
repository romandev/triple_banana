#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# Use of this source code is governed by a BSD-style license that can be
# found in the LICENSE file.

. $TOOLS_PATH/common/path.sh

function get_chromium_version() {
  cat $(triple_banana_path)/BASE
}
