// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.content.Context;

import androidx.annotation.Nullable;

public interface BananaSecureDnsSettings {
    static BananaSecureDnsSettings get() {
        return BananaInterfaceProvider.get(BananaSecureDnsSettings.class);
    }

    public String getSummary(@Nullable Context context);
}
