// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import org.chromium.base.CommandLine;

public enum ChromeExport {
    api;

    /**
     * Perform TripleBanana-specific command line initialization.
     *
     * @param commandLine CommandLine instance to be updated.
     */
    public void initCommandLine(CommandLine commandLine) {
        commandLine.appendSwitchWithValue(
                "enable-features", "ChromeDuet,HomePageButtonForceEnabled");
    }
}
