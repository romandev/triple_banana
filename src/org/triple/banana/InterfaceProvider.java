// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana;

import org.banana.cake.interfaces.BananaInterfaceProvider;

class InterfaceProvider {
    static void initialize() {
        BananaInterfaceProvider.register(
                org.banana.cake.interfaces.BananaCommandLineInitializer.class,
                org.triple.banana.CommandLineInitializer::new);
    }
}
