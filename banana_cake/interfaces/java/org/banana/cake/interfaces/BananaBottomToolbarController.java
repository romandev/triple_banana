// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.view.View;
import android.view.View.OnClickListener;

import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.tabmodel.IncognitoStateProvider;
import org.chromium.chrome.browser.toolbar.TabCountProvider;
import org.chromium.chrome.browser.toolbar.ThemeColorProvider;

public interface BananaBottomToolbarController {
    static BananaBottomToolbarController get() {
        return BananaInterfaceProvider.get(BananaBottomToolbarController.class);
    }

    BananaBottomToolbarController init(
            View root, ActivityTabProvider tabProvider, ThemeColorProvider themeColorProvider);
    void initializeWithNative(OnClickListener tabSwitcherListener,
            TabCountProvider tabCountProvider, IncognitoStateProvider incognitoStateProvider);
    boolean isEnabled();
    void destroy();
    void updateBookmarkButtonStatus(boolean isBookmarked, boolean editingAllowed);
    void updateBackButtonVisibility(boolean canGoBack);
    void updateForwardButtonVisibility(boolean canGoForward);
    void updateDesktopViewButtonState(boolean isDesktopView);
}
