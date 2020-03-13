// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.interfaces;

import android.content.Context;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;

public interface BananaPasswordExtension {
    static BananaPasswordExtension get() {
        return BananaInterfaceProvider.get(BananaPasswordExtension.class);
    }

    void overridePreferenceScreen(Context context, PreferenceScreen screen);
    void setupSecureLoginSwitch(View container);
    boolean isSecureLoginEnabled();
    void setSecureLoginEnabled();
}
