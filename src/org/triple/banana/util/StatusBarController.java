// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.util;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaSecureDnsBridge;
import org.banana.cake.interfaces.BananaStatusBarController;
import org.triple.banana.R;

public class StatusBarController implements BananaStatusBarController {
    @Override
    public @ColorInt int overrideStatusBarColor(@ColorInt int color) {
        if (!BananaApplicationUtils.get().isNativeInitialized()
                || !BananaSecureDnsBridge.get().isSecureMode())
            return color;

        return ContextCompat.getColor(BananaApplicationUtils.get().getApplicationContext(),
                R.color.secure_dns_status_bar_color);
    }
}
