// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import org.chromium.chrome.browser.ChromeApplication;

import org.triple.banana.public_api.internal.BananaChromeHooksImpl;

public class BananaApplication extends ChromeApplication implements BananaHooks {
    private static boolean wasHooksInitialized;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!wasHooksInitialized) {
            new BananaChromeHooksImpl(this);
        }
    }

    @Override
    public void initCommandLine(BananaCommandLine commandLine) {
    }
}
