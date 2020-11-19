// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana;

import android.text.TextUtils;

import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

import java.util.ArrayList;

class CommandLineInitializer implements BananaCommandLineInitializer {
    ArrayList<String> mBlinkFeatures = new ArrayList<>();
    ArrayList<String> mChromiumFeatures = new ArrayList<>();

    private void enableBlinkFeature(String featureName) {
        mBlinkFeatures.add(featureName);
    }

    private void enableChromiumFeature(String featureName) {
        mChromiumFeatures.add(featureName);
    }

    private void enableBlinkFeatures() {
        if (mBlinkFeatures.isEmpty()) return;
        BananaCommandLine.get().appendSwitchWithValue(
                "enable-blink-features", TextUtils.join(",", mBlinkFeatures));
    }

    private void enableChromiumFeatures() {
        if (mChromiumFeatures.isEmpty()) return;
        BananaCommandLine.get().appendSwitchWithValue(
                "enable-features", TextUtils.join(",", mChromiumFeatures));
    }

    @Override
    public void initCommandLine() {
        enableChromiumFeature("DarkenWebsitesCheckboxInThemesSetting");
        enableChromiumFeature("OmniboxSearchEngineLogo");

        if (ExtensionFeatures.isEnabled(FeatureName.BACKGROUND_PLAY)) {
            enableBlinkFeature("BackgroundPlay");
            BananaCommandLine.get().appendSwitchWithValue("disable-background-media-suspend", null);
        }

        if (ExtensionFeatures.isEnabled(FeatureName.SECURE_DNS)) {
            enableChromiumFeature("PostQuantumCECPQ2");
            enableChromiumFeature("DnsOverHttps");
        }

        if (ExtensionFeatures.isEnabled(FeatureName.MEDIA_REMOTE, true)) {
            enableBlinkFeature("MediaRemote");
        }

        if (!ExtensionFeatures.isEnabled(FeatureName.AUTOPLAY, true)) {
            BananaCommandLine.get().appendSwitchWithValue(
                    "autoplay-policy", "user-gesture-required");
        }

        enableBlinkFeatures();
        enableChromiumFeatures();
    }
}
