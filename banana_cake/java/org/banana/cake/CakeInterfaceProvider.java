// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import org.banana.cake.CakeApplicationUtils;
import org.banana.cake.CakeCommandLine;
import org.banana.cake.CakeTab;
import org.banana.cake.CakeTabManager;
import org.banana.cake.CakeToolbarManager;
import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaCommandLine;
import org.banana.cake.interfaces.BananaInterfaceProvider;
import org.banana.cake.interfaces.BananaTab;
import org.banana.cake.interfaces.BananaTabManager;
import org.banana.cake.interfaces.BananaToolbarManager;

public class CakeInterfaceProvider {
    public static void initialize() {
        BananaInterfaceProvider.register(BananaApplicationUtils.class, CakeApplicationUtils::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaCommandLine.class, CakeCommandLine::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaTab.class, CakeTab::new);
        BananaInterfaceProvider.register(BananaTabManager.class, CakeTabManager::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaToolbarManager.class, CakeToolbarManager::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
    }
}
