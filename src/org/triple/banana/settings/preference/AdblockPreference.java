// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import org.banana.cake.interfaces.BananaSwitchPreference;
import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;
import org.triple.banana.version.VersionInfo;

public class AdblockPreference extends BananaSwitchPreference {
    public AdblockPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTitle(String.format(context.getResources().getString(R.string.adblock_with_version),
                VersionInfo.getFilterVersion()));
        setChecked(ExtensionFeatures.isEnabled(FeatureName.ADBLOCK));
        setOnPreferenceChangeListener((preference, newValue) -> {
            ExtensionFeatures.setEnabled(FeatureName.ADBLOCK, (boolean) newValue);
            return true;
        });
    }
}
