// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import org.chromium.chrome.browser.tab.Tab;

public class BananaTab {
    private Tab mTab;

    public BananaTab(Tab tab) {
        mTab = tab;
    }

    /**
     * @return The URL that is currently visible in the location bar. This may not be the same as
     *         the last committed URL if a new navigation is in progress.
     */
    public String getUrl() {
        return mTab.getUrl();
    }

    /**
     * Injects the passed Javascript code in the current page and evaluates it.
     * If a result is required, pass in a callback.
     *
     * @param script The Javascript to execute.
     */
    public void evaluateJavaScript(String script) {
        mTab.getWebContents().evaluateJavaScript(script, null);
    }
}
