// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import android.app.Activity;

import org.chromium.base.ApplicationStatus;
import org.chromium.chrome.browser.ChromeActivity;
import org.chromium.chrome.browser.toolbar.ToolbarManager;

public enum BananaToolsApi {
    instance;

    private ToolbarManager mToolbarManager;

    private ToolbarManager getToolbarManager() {
        if (mToolbarManager == null) {
            Activity activity = ApplicationStatus.getLastTrackedFocusedActivity();
            if (!(activity instanceof ChromeActivity)) return null;

            ChromeActivity chromeActivity = (ChromeActivity) activity;
            mToolbarManager = chromeActivity.getToolbarManager();
        }
        return mToolbarManager;
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
    public void search() {}
    public void addNewTab() {}
    public void addBookmark() {}
    public void goBookmark() {}
    public void addBlock() {}
    public void changeSecretMode() {}
    public void addSecretTab() {}
    public void download() {}
    public void changeDesktopMode() {}
    public void findInpage() {}
    public void addHomeScreen() {}
    public void reload() {}
    public void goVisitHistory() {}
    public void print() {}
    public void goPasswordSetting() {}
    public void goArchive() {}
}
