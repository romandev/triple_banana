// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.hooks_api;

import org.chromium.chrome.browser.tab.Tab;

public class ChromeHooks {
    public static ChromeHooksDelegate sDelegate = new ChromeHooksDelegate() {};

    public static void setInstance(ChromeHooksDelegate delegate) {
        if (delegate != null) {
            sDelegate = delegate;
        }
    }

    public static ChromeHooksDelegate getInstance() {
        return sDelegate;
    }
}
