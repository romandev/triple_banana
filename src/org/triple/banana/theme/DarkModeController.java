// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.theme;

import android.content.SharedPreferences;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaSystemNightModeMonitor;

public class DarkModeController {
    private static final String UI_THEME_SETTING = "ui_theme_setting";
    private static final String UI_THEME_DARKEN_WEBSITES_ENABLED = "darken_websites_enabled";
    interface State {
        static final int SYSTEM_DEFAULT = 0;
        static final int LIGHT = 1;
        static final int DARK = 2;
    }

    private static DarkModeController sInstance;

    DarkModeController() {}

    public static DarkModeController get() {
        if (sInstance == null) {
            sInstance = new DarkModeController();
        }

        return sInstance;
    }

    private boolean isSystemNightModeOn() {
        return BananaSystemNightModeMonitor.get().isSystemNightModeOn();
    }

    private int getCurrentState() {
        return BananaApplicationUtils.get().getSharedPreferences().getInt(
                UI_THEME_SETTING, State.SYSTEM_DEFAULT);
    }

    private void setCurrentState(int state) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putInt(UI_THEME_SETTING, state);
        editor.putBoolean(UI_THEME_DARKEN_WEBSITES_ENABLED, true);
        editor.apply();
    }

    public void toggle() {
        if (isSystemNightModeOn()) {
            if (getCurrentState() == State.LIGHT)
                setCurrentState(State.SYSTEM_DEFAULT);
            else
                setCurrentState(State.LIGHT);
        } else {
            if (getCurrentState() == State.DARK)
                setCurrentState(State.SYSTEM_DEFAULT);
            else
                setCurrentState(State.DARK);
        }
    }

    public boolean isDarkModeOn() {
        return getCurrentState() == State.DARK
                || (getCurrentState() == State.SYSTEM_DEFAULT && isSystemNightModeOn());
    }
}
