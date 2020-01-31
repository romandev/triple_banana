// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import android.content.Context;

import org.banana.cake.interfaces.BananaContextUtils;

import org.chromium.base.ContextUtils;

class CakeContextUtils implements BananaContextUtils {
    @Override
    public Context getApplicationContext() {
        return ContextUtils.getApplicationContext();
    }
}
