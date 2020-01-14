// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.public_api.export;

import android.content.Context;

import org.chromium.base.ContextUtils;

public class BananaContextUtils {
    public static Context getApplicationContext() {
        return ContextUtils.getApplicationContext();
    }
}
