// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import org.banana.cake.interfaces.BananaBottomToolbarController;
import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.banana.cake.interfaces.BananaInterfaceProvider;
import org.banana.cake.interfaces.BananaPasswordExtension;
import org.banana.cake.interfaces.BananaToolbarEditor;
import org.triple.banana.CommandLineInitializer;
import org.triple.banana.password.PasswordExtension;
import org.triple.banana.toolbar.BottomToolbarController;
import org.triple.banana.toolbar.ToolbarEditor;

class InterfaceProvider {
    static void initialize() {
        BananaInterfaceProvider.register(BananaBottomToolbarController.class,
                BottomToolbarController::new, BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaCommandLineInitializer.class,
                CommandLineInitializer::new, BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaPasswordExtension.class, PasswordExtension::new,
                BananaInterfaceProvider.InstanceType.SINGLETON);
        BananaInterfaceProvider.register(BananaToolbarEditor.class,
                ToolbarEditor::new, BananaInterfaceProvider.InstanceType.SINGLETON);
    }
}
