// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.interfaces;

import android.view.View;
import android.view.View.OnClickListener;

import org.chromium.chrome.browser.ActivityTabProvider;
import org.chromium.chrome.browser.ThemeColorProvider;
import org.chromium.chrome.browser.compositor.layouts.OverviewModeBehavior;
import org.chromium.chrome.browser.toolbar.IncognitoStateProvider;
import org.chromium.chrome.browser.toolbar.MenuButton;
import org.chromium.chrome.browser.toolbar.TabCountProvider;
import org.chromium.chrome.browser.ui.appmenu.AppMenuButtonHelper;

public interface BananaBottomToolbarController {
    static BananaBottomToolbarController get() {
        return BananaInterfaceProvider.get(BananaBottomToolbarController.class);
    }

    BananaBottomToolbarController init(View root, ActivityTabProvider tabProvider);
    void initializeWithNative(OnClickListener tabSwitcherListener,
            AppMenuButtonHelper menuButtonHelper, OverviewModeBehavior overviewModeBehavior,
            TabCountProvider tabCountProvider, ThemeColorProvider themeColorProvider,
            IncognitoStateProvider incognitoStateProvider);
    void showAppMenuUpdateBadge();
    void removeAppMenuUpdateBadge();
    boolean isShowingAppMenuUpdateBadge();
    MenuButton getMenuButton();
    void destroy();
}
