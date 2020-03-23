// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
