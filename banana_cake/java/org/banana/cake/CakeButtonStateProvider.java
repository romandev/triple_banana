// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.app.Activity;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaButtonStateProvider;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeTabbedActivity;
import org.chromium.chrome.browser.ShortcutHelper;
import org.chromium.chrome.browser.download.DownloadUtils;
import org.chromium.chrome.browser.share.ShareUtils;
import org.chromium.chrome.browser.tab.Tab;
import org.chromium.chrome.browser.translate.TranslateUtils;
import org.chromium.components.embedder_support.util.UrlConstants;
import org.chromium.components.webapps.WebappsUtils;
import org.chromium.content_public.browser.NavigationController;

class CakeButtonStateProvider implements BananaButtonStateProvider {
    @Override
    public boolean canUseDownloadPage() {
        Tab currentTab = getActivityTab();
        if (currentTab == null) {
            return false;
        }
        return DownloadUtils.isAllowedToDownloadPage(currentTab);
    }

    @Override
    public boolean canUseShare() {
        ShareUtils shareUtils = new ShareUtils();
        return shareUtils.shouldEnableShare(getActivityTab());
    }

    @Override
    public boolean canUseAddToHome() {
        Tab currentTab = getActivityTab();
        if (currentTab == null) {
            return false;
        }
        String url = currentTab.getUrlString();
        boolean isChromeScheme = url.startsWith(UrlConstants.CHROME_URL_PREFIX)
                || url.startsWith(UrlConstants.CHROME_NATIVE_URL_PREFIX);
        boolean isFileScheme = url.startsWith(UrlConstants.FILE_URL_PREFIX);
        boolean isContentScheme = url.startsWith(UrlConstants.CONTENT_URL_PREFIX);
        boolean isIncognito = currentTab.isIncognito();

        return WebappsUtils.isAddToHomeIntentSupported() && !isChromeScheme && !isFileScheme
                && !isContentScheme && !isIncognito && !TextUtils.isEmpty(url);
    }

    @Override
    public boolean canUseTranslate() {
        Tab currentTab = getActivityTab();
        if (currentTab == null) {
            return false;
        }
        return TranslateUtils.canTranslateCurrentTab(currentTab);
    }

    @Override
    public boolean isWebContentAvailable() {
        final Tab currentTab = getActivityTab();
        if (currentTab == null) {
            return false;
        }
        return !currentTab.isNativePage() && currentTab.getWebContents() != null;
    }

    @Override
    public boolean isDesktopPage() {
        final Tab currentTab = getActivityTab();
        if (currentTab == null || currentTab.getWebContents() == null) {
            return false;
        }
        final NavigationController controller =
                currentTab.getWebContents().getNavigationController();
        if (controller == null) {
            return false;
        }
        return controller.getUseDesktopUserAgent();
    }

    private @Nullable Tab getActivityTab() {
        Activity activity = ApplicationStatus.getLastTrackedFocusedActivity();
        if (!(activity instanceof ChromeTabbedActivity)) {
            return null;
        }
        return ((ChromeTabbedActivity) activity).getActivityTab();
    }
}
