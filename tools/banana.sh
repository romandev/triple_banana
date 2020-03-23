#!/bin/bash
#
# Copyright 2019 The Triple Banana Authors. All rights reserved.
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
