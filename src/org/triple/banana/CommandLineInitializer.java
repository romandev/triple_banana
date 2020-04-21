// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana;

import android.os.Build;
import android.text.TextUtils;

import org.banana.cake.interfaces.BananaBuildConfig;
import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

import java.util.ArrayList;

class CommandLineInitializer implements BananaCommandLineInitializer {
    @Override
    public void initCommandLine() {
        if (BananaBuildConfig.IS_ARM64 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BananaCommandLine.get().appendSwitchWithValue("disable_aimagereader", "1");
        }
        if (ExtensionFeatures.isEnabled(FeatureName.BACKGROUND_PLAY)) {
            BananaCommandLine.get().appendSwitchWithValue(
                    "enable-blink-features", "BackgroundPlay");
            BananaCommandLine.get().appendSwitchWithValue("disable-media-suspend", null);
        }
        ArrayList<String> enableFeatures = new ArrayList<>();
        enableFeatures.add("DarkenWebsitesCheckboxInThemesSetting");
        if (ExtensionFeatures.isEnabled(FeatureName.BOTTOM_TOOLBAR, true)) {
            enableFeatures.add("ChromeDuet,HomePageButtonForceEnabled,OmniboxSearchEngineLogo");
        }
        if (ExtensionFeatures.isEnabled(FeatureName.SECURE_DNS)) {
            enableFeatures.add("PostQuantumCECPQ2,DnsOverHttps");
        }
        if (!enableFeatures.isEmpty()) {
            BananaCommandLine.get().appendSwitchWithValue(
                    "enable-features", TextUtils.join(",", enableFeatures));
        }
    }
}
