// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import org.banana.cake.interfaces.BananaFeatureFlags;

import org.chromium.chrome.browser.site_settings.WebsitePreferenceBridge;
import org.chromium.components.content_settings.ContentSettingsType;

public class CakeFeatureFlags implements BananaFeatureFlags {
    @Override
    public boolean isAdblockEnabled() {
        return !WebsitePreferenceBridge.isCategoryEnabled(ContentSettingsType.ADS);
    }

    @Override
    public void setAdblockEnabled(boolean value) {
        WebsitePreferenceBridge.setCategoryEnabled(ContentSettingsType.ADS, !value);
    }
}
