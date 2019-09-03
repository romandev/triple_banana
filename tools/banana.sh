#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# Use of this source code is governed by a BSD-style license that can be
# found in the LICENSE file.

. $TOOLS_PATH/common/path.sh

for command in $(ls $(triple_banana_command_path)); do
  if [ "$1" = "$command" ]; then
    shift
    $(triple_banana_command_path)/$command $@
    exit
  fi
done

echo "Usage:"
echo "  banana ["$(ls -m $(triple_banana_command_path))"]"
