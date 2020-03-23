// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.bootstrap;

import org.banana.cake.CakeInterfaceProvider;

import org.chromium.chrome.browser.ChromeApplication;

public class BananaApplication extends ChromeApplication {
    static {
        CakeInterfaceProvider.initialize();
    }
}
