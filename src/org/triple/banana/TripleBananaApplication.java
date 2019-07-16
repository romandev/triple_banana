// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import org.triple.banana.public_api.export.BananaApplication;
import org.triple.banana.public_api.export.BananaCommandLine;

public class TripleBananaApplication extends BananaApplication {
    @Override
    public void initCommandLine(BananaCommandLine commandLine) {
        commandLine.appendSwitchWithValue(
                "enable-features", "ChromeDuet,HomePageButtonForceEnabled");
    }
}
