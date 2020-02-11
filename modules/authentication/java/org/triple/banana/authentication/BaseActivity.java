// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import org.triple.banana.base.InterActivity;

/**
 * BaseActivity
 */
public class BaseActivity extends InterActivity<Object, Boolean> {
    @Override
    public void finish() {
        super.finish();
        // Disable animation when finishing activity.
        overridePendingTransition(0, 0);
    }
}
