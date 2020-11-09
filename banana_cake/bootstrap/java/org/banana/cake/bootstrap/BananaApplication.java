// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.bootstrap;

import android.content.Context;

import org.banana.cake.CakeInterfaceProvider;

import org.chromium.chrome.browser.ChromeApplication;
import org.chromium.chrome.browser.init.ChromeBrowserInitializer;

public abstract class BananaApplication extends ChromeApplication {
    static {
        CakeInterfaceProvider.initialize();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (isBrowserProcess()) {
            onBeforeInitialized();
            runNowOrAfterFullBrowserStarted(() -> { onInitialized(); });
        }
    }

    protected void runNowOrAfterFullBrowserStarted(Runnable task) {
        ChromeBrowserInitializer.getInstance().runNowOrAfterFullBrowserStarted(task);
    }

    protected abstract void onBeforeInitialized();
    protected abstract void onInitialized();
}
