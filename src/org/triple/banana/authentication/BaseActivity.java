// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.authentication;

import android.os.Bundle;

import org.triple.banana.R;
import org.triple.banana.base.InterActivity;

/**
 * BaseActivity
 */
public class BaseActivity extends InterActivity<Boolean /* background */, Boolean /* result */> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean isBackground = getRequestData();
        if (Boolean.TRUE.equals(isBackground)) {
            setContentView(R.layout.browser_lock_background);
        }
    }
    @Override
    public void finish() {
        super.finish();
        // Disable animation when finishing activity.
        overridePendingTransition(0, 0);
    }
}
