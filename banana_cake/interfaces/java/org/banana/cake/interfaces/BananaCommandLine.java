// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.interfaces;

public interface BananaCommandLine {
    static BananaCommandLine get() {
        return BananaInterfaceProvider.get(BananaCommandLine.class);
    }

    /**
     * Appends a switch to the current list.
     *
     * @param switchString the switch to add.  It should NOT start with '--' !
     * @param value the value for this switch.
     */
    void appendSwitchWithValue(String switchString, String value);
}
