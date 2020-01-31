// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

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
