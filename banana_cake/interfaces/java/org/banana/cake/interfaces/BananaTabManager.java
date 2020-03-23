// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import org.chromium.chrome.browser.tab.Tab;

public interface BananaTabManager {
    static BananaTabManager get() {
        return BananaInterfaceProvider.get(BananaTabManager.class);
    }

    void addObserver(BananaTabObserver observer);
    void removeObserver(BananaTabObserver observer);

    void notifyUrlUpdated(Tab tab);
}
