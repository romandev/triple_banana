// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import org.chromium.chrome.browser.ChromeApplication;

public class BananaApplication extends ChromeApplication {
    private static boolean sWasInitialized;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!sWasInitialized) {
            sWasInitialized = onInitializeHooks(BananaHooks.getInstance());
        }
    }

    public boolean onInitializeHooks(BananaHooks hooks) {
        return true;
    }
}
