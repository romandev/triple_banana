// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.hooks_api;

import android.content.Context;
import android.view.View;

import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.toolbar.bottom.BottomToolbarCoordinator.BottomToolbarCoordinatorDelegate;

public interface ChromeHooksDelegate {
    default void initCommandLine() {}
    default void onUrlUpdated(Tab tab) {}
    default BottomToolbarCoordinatorDelegate createBottomToolbarCoordinatorDelegate(
            View root, ActivityTabProvider tabProvider) {
        return null;
    }
    default void startToolbarEditActivity(Context packageContext) {}
}
