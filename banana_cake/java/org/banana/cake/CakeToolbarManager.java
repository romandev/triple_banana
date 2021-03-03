// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeTabbedActivity;
import org.chromium.chrome.browser.banners.AppBannerManager;
import org.chromium.chrome.browser.banners.AppMenuVerbiage;
import org.chromium.chrome.browser.browsing_data.ClearBrowsingDataTabsFragment;
import org.chromium.chrome.browser.omnibox.OmniboxFocusReason;
import org.chromium.chrome.browser.settings.SettingsLauncherImpl;
import org.chromium.chrome.browser.share.qrcode.QrCodeCoordinator;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.toolbar.ToolbarManager;

public class CakeToolbarManager implements BananaToolbarManager {
    private @Nullable ToolbarManager getToolbarManager() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return null;
        return activity.getToolbarManager();
    }

    private @Nullable ChromeTabbedActivity getChromeTabbedActivity() {
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
        toolbarManager.setUrlBarFocus(true, OmniboxFocusReason.ACCELERATOR_TAP);
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
        // FIXME(#896): Rather than creating the bundle directly, use getBundleForMenuItem
        Bundle bundle = new Bundle();
        bundle.putInt(
                AppBannerManager.MENU_TITLE_KEY, AppMenuVerbiage.APP_MENU_OPTION_ADD_TO_HOMESCREEN);
        activity.onOptionsItemSelected(R.id.add_to_homescreen_id, bundle);
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
    public void print() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.print_id, false);
    }

    @Override
    public void goAdblockSetting() {
        // NOTIMPLEMENTED
    }

    @Override
    public void openSettingPage(Class settingFragmentClass) {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        SettingsLauncherImpl settingsLauncher = new SettingsLauncherImpl();
        settingsLauncher.launchSettingsActivity(activity, settingFragmentClass);
    }

    @Override
    public void openClearBrowsingDataPreference() {
        openSettingPage(ClearBrowsingDataTabsFragment.class);
    }

    @Override
    public void terminate() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.finishAndRemoveTask();
    }

    @Override
    public void closeCurrentTab() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.close_tab, false);
    }

    @Override
    public void openQRCodeDialog() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;

        Tab tab = activity.getActivityTab();
        String url;
        if (tab == null) {
            url = null;
        } else {
            url = tab.getUrl().getSpec();
        }

        QrCodeCoordinator qrCodeCoordinator = new QrCodeCoordinator(activity, url);
        qrCodeCoordinator.show();
    }

    @Override
    public boolean getUseDesktopUserAgent() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return false;
        Tab tab = activity.getActivityTab();
        if (tab == null || tab.getWebContents() == null) return false;
        return tab.getWebContents().getNavigationController().getUseDesktopUserAgent();
    }

    @Override
    public void translateCurrentTab() {
        ChromeTabbedActivity activity = getChromeTabbedActivity();
        if (activity == null) return;
        activity.onMenuOrKeyboardAction(R.id.translate_id, false);
    }
}
