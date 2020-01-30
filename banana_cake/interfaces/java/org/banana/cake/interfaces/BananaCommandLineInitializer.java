// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.interfaces;

public interface BananaCommandLineInitializer {
    static BananaCommandLineInitializer get() {
        return BananaInterfaceProvider.get(BananaCommandLineInitializer.class);
    }

    void initCommandLine();
}
