// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

public interface BananaToolbarManager {
    static BananaToolbarManager get() {
        return BananaInterfaceProvider.get(BananaToolbarManager.class);
    }

    void back();
    void forward();
    void share();
    void search();
    void addNewTab();
    void addBookmark();
    void goBookmark();
    void addSecretTab();
    void download();
    void changeDesktopMode();
    void findInPage();
    void addToHomeScreen();
    void reload();
    void goVisitHistory();
    void goArchive();
    void print();
    void goAdblockSetting();
    void openSettingPage(Class settingFragmentPage);
    void openClearBrowsingDataPreference();
    void terminate();
    void closeCurrentTab();
    void openQRCodeDialog();
    boolean getUseDesktopUserAgent();
    void translateCurrentTab();
}
