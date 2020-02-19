package org.triple.banana;

import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

class CommandLineInitializer implements BananaCommandLineInitializer {
    @Override
    public void initCommandLine() {
        BananaCommandLine.get().appendSwitchWithValue(
                "enable-features", "ChromeDuet,HomePageButtonForceEnabled");
        if (ExtensionFeatures.isEnabled(FeatureName.BACKGROUND_PLAY)) {
            BananaCommandLine.get().appendSwitchWithValue(
                    "enable-blink-features", "BackgroundPlay");
            BananaCommandLine.get().appendSwitchWithValue("disable-media-suspend", null);
        }
    }
}
