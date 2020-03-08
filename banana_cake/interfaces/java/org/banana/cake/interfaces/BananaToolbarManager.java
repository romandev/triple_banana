// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

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
}
