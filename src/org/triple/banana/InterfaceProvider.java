// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import org.banana.cake.interfaces.BananaCommandLineInitializer;
import org.banana.cake.interfaces.BananaInterfaceProvider;
import org.triple.banana.CommandLineInitializer;

class InterfaceProvider {
    static void initialize() {
        BananaInterfaceProvider.register(BananaCommandLineInitializer.class,
                CommandLineInitializer::new, BananaInterfaceProvider.InstanceType.SINGLETON);
    }
}
