// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import android.app.Activity;

import org.triple.banana.R;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeTabbedActivity;
import org.chromium.chrome.browser.omnibox.LocationBar;
import org.chromium.chrome.browser.toolbar.ToolbarManager;

public enum BananaToolsApi {
    instance;

    private ToolbarManager mToolbarManager;

    private ToolbarManager getToolbarManager() {
        if (mToolbarManager == null) {
            ChromeTabbedActivity activity = getChromeTabbedActivity();
            if (activity == null) return null;
            mToolbarManager = activity.getToolbarManager();
        }
        return mToolbarManager;
    }

    private ChromeTabbedActivity getChromeTabbedActivity() {
        Activity activity = ApplicationStatus.getLastTrackedFocusedActivity();
        if (!(activity instanceof ChromeTabbedActivity)) return null;
        return (ChromeTabbedActivity) activity;
    }

    public void back() {
        ToolbarManager toolbarManager = getToolbarManager();
        if (toolbarManager == null) return;
        toolbarManager.back();
    }

    public void forward() {
        ToolbarManager toolbarManager = getToolbarManager();
        if (toolbarManager == null) return;
        toolbarManager.forward();
    }

    public void share() {}

    public void search() {
        ToolbarManager toolbarManager = getToolbarManager();
        if (toolbarManager == null) return;
        toolbarManager.setUrlBarFocus(true, LocationBar.OmniboxFocusReason.ACCELERATOR_TAP);
    }

    public void addNewTab() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.new_tab_menu_id, false);
    }

    public void addBookmark() {}

    public void goBookmark() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.all_bookmarks_menu_id, false);
    }

    public void addBlock() {}
    public void changeSecretMode() {}

    public void addSecretTab() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.new_incognito_tab_menu_id, false);
    }

    public void download() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.downloads_menu_id, false);
    }

    public void changeDesktopMode() {}
    public void findInpage() {}
    public void addHomeScreen() {}
    public void reload() {}
    public void goVisitHistory() {}
    public void print() {}
    public void goPasswordSetting() {}
    public void goArchive() {}
}
