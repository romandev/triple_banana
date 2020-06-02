// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.content.Context;
import android.view.View;

import androidx.preference.PreferenceScreen;

public interface BananaPasswordExtension {
    static BananaPasswordExtension get() {
        return BananaInterfaceProvider.get(BananaPasswordExtension.class);
    }

    void overridePreferenceScreen(Context context, PreferenceScreen screen);
    void setupSecureLoginSwitch(View container);
    boolean isSecureLoginEnabled();
    void setSecureLoginEnabled();
}
