// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import org.banana.cake.interfaces.BananaSwitchPreference;
import org.triple.banana.remote_config.RemoteConfig;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class BackgroundPlayPreference extends RestartSwitchPreference {
    private RemoteConfig mRemoteConfig =
            new RemoteConfig("https://zino.dev/triple_banana_config/remote_config.json");

    public BackgroundPlayPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        boolean isVisible = ExtensionFeatures.wasSetByUser(FeatureName.BACKGROUND_PLAY);
        setVisible(isVisible);
        if (!isVisible) {
            mRemoteConfig.getAsync(config -> {
                boolean isRemoteEnabled = config.optBoolean("background_play");
                if (isRemoteEnabled) {
                    ExtensionFeatures.setEnabled(FeatureName.BACKGROUND_PLAY, isChecked());
                    setVisible(true);
                }
            });
        }
    }
}
