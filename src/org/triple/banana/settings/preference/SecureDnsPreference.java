// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import org.triple.banana.secure_dns.SecureDnsNotificationManager;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class SecureDnsPreference extends RestartSwitchPreference {
    public SecureDnsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChecked(ExtensionFeatures.isEnabled(FeatureName.SECURE_DNS));
        setOnPreferenceChangeListener((preference, newValue) -> {
            if (((boolean) newValue)
                    && SecureDnsNotificationManager.getInstance().wasNotificationShown()) {
                SecureDnsNotificationManager.getInstance().resetNotificationState();
            }
            return true;
        });
    }
}
