// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.theme;

public class DarkModeController {
    private static DarkModeController sInstance;
    private static Boolean mSetting;

    DarkModeController() {}

    public static DarkModeController get() {
        if (sInstance == null) {
            sInstance = new DarkModeController();
        }

        // TODO(bk_1.ko) : It will be updated from UI_THEME_SETTING
        mSetting = false;

        return sInstance;
    }

    public void toggle() {
        // TODO(bk_1.ko) : implement for toggle() api.
    }

    public Boolean isDarkModeOn() {
        // TODO(bk_1.ko) : implement for isDarkModeOn() api
        return mSetting;
    }
}