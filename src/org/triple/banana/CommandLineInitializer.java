package org.triple.banana;

import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaCommandLineInitializer;

class CommandLineInitializer implements BananaCommandLineInitializer {
    @Override
    public void initCommandLine() {
        BananaCommandLine.get().appendSwitchWithValue(
                "enable-features", "ChromeDuet,HomePageButtonForceEnabled");
    }
}
