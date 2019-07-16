// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import org.chromium.base.CommandLine;

public enum BananaCommandLine {
    instance;

    /**
     * Appends a switch to the current list.
     *
     * @param switchString the switch to add.  It should NOT start with '--' !
     * @param value the value for this switch.
     */
    public void appendSwitchWithValue(String switchString, String value) {
        CommandLine.getInstance().appendSwitchWithValue(switchString, value);
    }
}
