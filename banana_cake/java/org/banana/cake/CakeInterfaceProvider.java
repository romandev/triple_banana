// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import org.banana.cake.CakeApplicationUtils;
import org.banana.cake.CakeClearBrowsingData;
import org.banana.cake.CakeCommandLine;
import org.banana.cake.CakeDarkModeUtils;
import org.banana.cake.CakeFeatureFlags;
import org.banana.cake.CakePipController;
import org.banana.cake.CakeSubresourceFilter;
import org.banana.cake.CakeTab;
import org.banana.cake.CakeTabManager;
import org.banana.cake.CakeToolbarManager;
import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaDarkModeUtils;
import org.banana.cake.interfaces.BananaFeatureFlags;
import org.banana.cake.interfaces.BananaInterfaceProvider;
import org.banana.cake.interfaces.BananaPipController;
import org.banana.cake.interfaces.BananaSubresourceFilter;
import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;
import org.banana.cake.interfaces.BananaToolbarManager;

public class CakeInterfaceProvider {
    public static void initialize() {
        BananaInterfaceProvider.register(BananaApplicationUtils.class, CakeApplicationUtils::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaCommandLine.class, CakeCommandLine::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaFeatureFlags.class, CakeFeatureFlags::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaDarkModeUtils.class, CakeDarkModeUtils::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaPipController.class, CakePipController::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaSubresourceFilter.class, CakeSubresourceFilter::new);
        BananaInterfaceProvider.register(BananaTab.class, CakeTab::new);
        BananaInterfaceProvider.register(BananaTabManager.class, CakeTabManager::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaToolbarManager.class, CakeToolbarManager::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaClearBrowsingData.class, CakeClearBrowsingData::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
    }
}
