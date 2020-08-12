// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import org.banana.cake.interfaces.BananaFeatureFlags;

import org.chromium.chrome.browser.preferences.Pref;
import org.chromium.chrome.browser.profiles.Profile;
import org.chromium.components.browser_ui.site_settings.WebsitePreferenceBridge;
import org.chromium.components.content_settings.ContentSettingsType;
import org.chromium.components.prefs.PrefService;
import org.chromium.components.user_prefs.UserPrefs;

public class CakeFeatureFlags implements BananaFeatureFlags {
    @Override
    public boolean isAdblockEnabled() {
        return !WebsitePreferenceBridge.isCategoryEnabled(
                Profile.getLastUsedRegularProfile(), ContentSettingsType.ADS);
    }

    @Override
    public void setAdblockEnabled(boolean value) {
        WebsitePreferenceBridge.setCategoryEnabled(
                Profile.getLastUsedRegularProfile(), ContentSettingsType.ADS, !value);
    }

    @Override
    public boolean isTranslateEnabled() {
        return getPrefService().getBoolean(Pref.OFFER_TRANSLATE_ENABLED);
    }

    @Override
    public void setTranslateEnabled(boolean value) {
        getPrefService().setBoolean(Pref.OFFER_TRANSLATE_ENABLED, value);
    }

    private static PrefService getPrefService() {
        return UserPrefs.get(Profile.getLastUsedRegularProfile());
    }
}
