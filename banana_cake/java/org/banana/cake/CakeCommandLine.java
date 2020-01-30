// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import org.banana.cake.interfaces.BananaCommandLine;

import org.chromium.base.CommandLine;

class CakeCommandLine implements BananaCommandLine {
    @Override
    public void appendSwitchWithValue(String switchString, String value) {
        CommandLine.getInstance().appendSwitchWithValue(switchString, value);
    }
}
