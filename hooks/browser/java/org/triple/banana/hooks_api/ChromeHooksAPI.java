// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.hooks_api;

import org.chromium.chrome.browser.tab.Tab;

public interface ChromeHooksAPI {
    void initCommandLine();
    void onUrlUpdated(Tab tab);
}
