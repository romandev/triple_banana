// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana;

import org.banana.cake.interfaces.BananaAppMenu;
import org.banana.cake.interfaces.BananaApplicationEvent;
import org.banana.cake.interfaces.BananaBottomToolbarController;
import org.banana.cake.interfaces.BananaButtonStateManager;
import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.banana.cake.interfaces.BananaExtensionSettings;
import org.banana.cake.interfaces.BananaInterfaceProvider;
import org.banana.cake.interfaces.BananaPasswordExtension;
import org.banana.cake.interfaces.BananaVersionInfo;
import org.triple.banana.CommandLineInitializer;
import org.triple.banana.appmenu.AppMenuDelegate;
import org.triple.banana.browsing_data.AutoClearBrowsingData;
import org.triple.banana.button_state.ButtonStateManager;
import org.triple.banana.password.PasswordExtension;
import org.triple.banana.settings.SettingsOpener;
import org.triple.banana.toolbar.BottomToolbarController;
import org.triple.banana.version.VersionInfo;

class InterfaceProvider {
    static void initialize() {
        BananaInterfaceProvider.register(BananaAppMenu.class, AppMenuDelegate::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaBottomToolbarController.class,
                BottomToolbarController::new, BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaCommandLineInitializer.class,
                CommandLineInitializer::new, BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaExtensionSettings.class, SettingsOpener::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaPasswordExtension.class, PasswordExtension::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaVersionInfo.class, VersionInfo::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaApplicationEvent.class, AutoClearBrowsingData::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaButtonStateManager.class,
                ButtonStateManager::getInstance, BananaInterfaceProvider.InstanceType.SINGLETON);
    }
}
