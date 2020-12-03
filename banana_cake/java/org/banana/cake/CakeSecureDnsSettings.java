// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.content.Context;

import androidx.annotation.Nullable;

import org.banana.cake.interfaces.BananaSecureDnsSettings;

import org.chromium.chrome.browser.privacy.secure_dns.SecureDnsSettings;

public class CakeSecureDnsSettings implements BananaSecureDnsSettings {
    @Override
    public String getSummary(@Nullable Context context) {
        if (context == null) {
            return new String();
        }
        return SecureDnsSettings.getSummary(context);
    }
}
