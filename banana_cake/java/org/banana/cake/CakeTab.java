// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import org.banana.cake.interfaces.BananaTab;

import org.chromium.chrome.browser.tab.Tab;

class CakeTab implements BananaTab {
    private Tab mTab;

    @Override
    public BananaTab wrap(Tab tab) {
        mTab = tab;
        return this;
    }

    /**
     * @return The URL that is currently visible in the location bar. This may not be the same as
     *         the last committed URL if a new navigation is in progress.
     */
    @Override
    public String getUrl() {
        if (mTab == null) return new String();
        return mTab.getUrl();
    }

    /**
     * Injects the passed Javascript code in the current page and evaluates it.
     * If a result is required, pass in a callback.
     *
     * @param script The Javascript to execute.
     */
    @Override
    public void evaluateJavaScript(String script) {
        if (mTab == null) return;
        mTab.getWebContents().evaluateJavaScript(script, null);
    }
}
