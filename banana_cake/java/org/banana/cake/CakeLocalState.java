// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaLocalState;

import org.chromium.base.annotations.NativeMethods;
import org.chromium.chrome.browser.preferences.Pref;

public class CakeLocalState implements BananaLocalState {
    @Override
    public boolean isSecureDNSMode() {
        return !CakeLocalStateJni.get().getString(Pref.DNS_OVER_HTTPS_MODE).equals("off");
    }

    @NativeMethods
    interface Natives {
        @NonNull
        String getString(@NonNull String key);
    }
}
