// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.hooks_api;

import org.chromium.chrome.browser.tab.Tab;

public enum ChromeHooks implements ChromeHooksAPI {
    api;

    private ChromeHooksAPI mImpl;

    private ChromeHooksAPI getImpl() {
        assert mImpl != null;
        return mImpl;
    }

    public void setImpl(ChromeHooksAPI impl) {
        mImpl = impl;
    }

    @Override
    public void initCommandLine() {
        getImpl().initCommandLine();
    }

    @Override
    public void onUrlUpdated(Tab tab) {
        getImpl().onUrlUpdated(tab);
    }
}
