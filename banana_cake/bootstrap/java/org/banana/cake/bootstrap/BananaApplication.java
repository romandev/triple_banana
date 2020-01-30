// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.bootstrap;

import org.banana.cake.CakeInterfaceProvider;

import org.chromium.chrome.browser.ChromeApplication;

public class BananaApplication extends ChromeApplication {
    static {
        CakeInterfaceProvider.initialize();
    }
}
