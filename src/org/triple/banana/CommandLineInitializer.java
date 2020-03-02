// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import android.os.Build;

import org.banana.cake.interfaces.BananaBuildConfig;
import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

class CommandLineInitializer implements BananaCommandLineInitializer {
    @Override
    public void initCommandLine() {
        if (BananaBuildConfig.IS_ARM64 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BananaCommandLine.get().appendSwitchWithValue("disable_aimagereader", "1");
        }
        if (ExtensionFeatures.isEnabled(FeatureName.BOTTOM_TOOLBAR, true)) {
            BananaCommandLine.get().appendSwitchWithValue("enable-features",
                    "ChromeDuet,HomePageButtonForceEnabled,OmniboxSearchEngineLogo");
        }
        if (ExtensionFeatures.isEnabled(FeatureName.BACKGROUND_PLAY)) {
            BananaCommandLine.get().appendSwitchWithValue(
                    "enable-blink-features", "BackgroundPlay");
            BananaCommandLine.get().appendSwitchWithValue("disable-media-suspend", null);
        }
    }
}
