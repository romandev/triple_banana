// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import org.banana.cake.interfaces.BananaFeatureFlags;

import org.chromium.chrome.browser.ContentSettingsType;
import org.chromium.chrome.browser.settings.website.WebsitePreferenceBridge;

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
