// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import android.app.Activity;

import org.banana.cake.interfaces.BananaToolbarEditor;
import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeTabbedActivity;
import org.chromium.chrome.browser.omnibox.LocationBar;
import org.chromium.chrome.browser.preferences.PreferencesLauncher;
import org.chromium.chrome.browser.toolbar.ToolbarManager;

public class CakeToolbarManager implements BananaToolbarManager {
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

    @Override
    public void back() {
        ToolbarManager toolbarManager = getToolbarManager();
        if (toolbarManager == null) return;
        toolbarManager.back();
    }

    @Override
    public void forward() {
        ToolbarManager toolbarManager = getToolbarManager();
        if (toolbarManager == null) return;
        toolbarManager.forward();
    }

    @Override
    public void share() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.share_menu_id, false);
    }

    @Override
    public void search() {
        ToolbarManager toolbarManager = getToolbarManager();
        if (toolbarManager == null) return;
        toolbarManager.setUrlBarFocus(true, LocationBar.OmniboxFocusReason.ACCELERATOR_TAP);
    }

    @Override
    public void addNewTab() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.new_tab_menu_id, false);
    }

    @Override
    public void addBookmark() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.bookmark_this_page_id, false);
    }

    @Override
    public void goBookmark() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.all_bookmarks_menu_id, false);
    }

    @Override
    public void addSecretTab() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.new_incognito_tab_menu_id, false);
    }

    @Override
    public void download() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.offline_page_id, false);
    }

    @Override
    public void changeDesktopMode() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.request_desktop_site_id, false);
    }

    @Override
    public void findInPage() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.find_in_page_id, false);
    }

    @Override
    public void addToHomeScreen() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.add_to_homescreen_id, false);
    }

    @Override
    public void reload() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.reload_menu_id, false);
    }

    @Override
    public void goVisitHistory() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.open_history_menu_id, false);
    }

    @Override
    public void goArchive() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.downloads_menu_id, false);
    }

    @Override
    public void goPasswordSetting() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        PreferencesLauncher.showPasswordSettings(activity, 0);
    }

    @Override
    public void print() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.print_id, false);
    }

    @Override
    public void goAdblockSetting() {
        // NOTIMPLEMENTED
    }
}
