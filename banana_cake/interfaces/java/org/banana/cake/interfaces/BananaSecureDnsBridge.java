// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

public interface BananaSecureDnsBridge {
    static BananaSecureDnsBridge get() {
        return BananaInterfaceProvider.get(BananaSecureDnsBridge.class);
    }

    boolean isSecureDNSMode();
    void setSecureDNSMode(boolean isSecure);
}