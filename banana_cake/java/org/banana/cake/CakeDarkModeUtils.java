// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import org.banana.cake.interfaces.BananaDarkModeUtils;

import org.chromium.chrome.browser.night_mode.NightModeUtils;
import org.chromium.chrome.browser.night_mode.SystemNightModeMonitor;

class CakeDarkModeUtils implements BananaDarkModeUtils {
    @Override
    public boolean isSystemDarkModeOn() {
        return SystemNightModeMonitor.getInstance().isSystemNightModeOn();
    }

    @Override
    public boolean isDarkModeDefaultToLight() {
        return NightModeUtils.isNightModeDefaultToLight();
    }
}
