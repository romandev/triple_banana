// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import org.banana.cake.interfaces.BananaSecureDnsBridge;

import org.chromium.chrome.browser.privacy.secure_dns.SecureDnsBridge;
import org.chromium.net.SecureDnsMode;

public class CakeSecureDnsBridge implements BananaSecureDnsBridge {
    @Override
    public boolean isSecureMode() {
        int mode = SecureDnsBridge.getMode();
        return mode == SecureDnsMode.SECURE;
    }

    @Override
    public void setSecureMode(boolean enabled) {
        SecureDnsBridge.setMode(enabled ? SecureDnsMode.SECURE : SecureDnsMode.OFF);
        SecureDnsBridge.setTemplates(enabled ? "https://chrome.cloudflare-dns.com/dns-query" : "");
    }
}
