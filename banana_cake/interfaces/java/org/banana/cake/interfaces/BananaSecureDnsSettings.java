// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference.OnPreferenceChangeListener;

import org.chromium.chrome.browser.privacy.secure_dns.SecureDnsSettings;

public class BananaSecureDnsSettings extends SecureDnsSettings {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        super.onCreatePreferences(savedInstanceState, rootKey);
        final OnPreferenceChangeListener listener =
                mSecureDnsSwitch.getOnPreferenceChangeListener();
        mSecureDnsSwitch.setOnPreferenceChangeListener((preference, enabled) -> {
            if (listener != null) {
                listener.onPreferenceChange(preference, enabled);
                setSecureDnsEnabled((boolean) enabled);
                BananaApplicationUtils.get().showRestartDialog(getContext());
            }
            return true;
        });
    }

    private void setSecureDnsEnabled(final boolean enabled) {
        final SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean("feature_name_secure_dns", enabled);

        final boolean wasNotificationShown =
                BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                        "feature_secure_dns_notification_shown", false);
        if (enabled && wasNotificationShown) {
            editor.putBoolean("feature_secure_dns_notification_shown", false);
        }
        editor.apply();
    }
}
