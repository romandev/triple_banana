// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.toolbar;

import android.view.View.OnClickListener;

import org.chromium.chrome.browser.ThemeColorProvider;
import org.chromium.chrome.browser.appmenu.AppMenuButtonHelper;
import org.chromium.chrome.browser.compositor.layouts.OverviewModeBehavior;
import org.chromium.chrome.browser.toolbar.IncognitoStateProvider;
import org.chromium.chrome.browser.toolbar.MenuButton;
import org.chromium.chrome.browser.toolbar.TabCountProvider;
import org.chromium.chrome.browser.toolbar.bottom.BottomToolbarCoordinator.BottomToolbarCoordinatorDelegate;

public class BottomToolbarController implements BottomToolbarCoordinatorDelegate {
    @Override
    public void initializeWithNative(OnClickListener tabSwitcherListener,
            AppMenuButtonHelper menuButtonHelper, OverviewModeBehavior overviewModeBehavior,
            TabCountProvider tabCountProvider, ThemeColorProvider themeColorProvider,
            IncognitoStateProvider incognitoStateProvider) {}

    /**
     * Show the update badge over the bottom toolbar's app menu.
     */
    @Override
    public void showAppMenuUpdateBadge() {}

    /**
     * Remove the update badge.
     */
    @Override
    public void removeAppMenuUpdateBadge() {}

    /**
     * @return Whether the update badge is showing.
     */
    @Override
    public boolean isShowingAppMenuUpdateBadge() {
        return false;
    }

    /**
     * @return The browsing mode bottom toolbar's menu button.
     */
    @Override
    public MenuButton getMenuButton() {
        return null;
    }

    @Override
    public void destroy() {}
}
