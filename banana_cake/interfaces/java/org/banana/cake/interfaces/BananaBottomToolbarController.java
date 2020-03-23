// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
    void updateBookmarkButtonStatus(boolean isBookmarked, boolean editingAllowed);
    void updateBackButtonVisibility(boolean canGoBack);
    void updateForwardButtonVisibility(boolean canGoForward);
    void updateDesktopViewButtonState(boolean isDesktopView);
}
