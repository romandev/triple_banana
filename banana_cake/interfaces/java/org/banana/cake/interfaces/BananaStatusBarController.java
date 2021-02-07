// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaInterfaceProvider;

public interface BananaStatusBarController {
    static @NonNull BananaStatusBarController get() {
        return BananaInterfaceProvider.get(BananaStatusBarController.class);
    }

    @ColorInt
    int overrideStatusBarColor(@ColorInt int color);
}
