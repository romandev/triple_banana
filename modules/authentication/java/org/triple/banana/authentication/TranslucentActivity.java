// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import android.app.Activity;

/**
 * Translucent Activity for technical reasons
 */
public class TranslucentActivity extends Activity {
    @Override
    public void finish() {
        super.finish();
        // Disable animation when finishing activity.
        overridePendingTransition(0, 0);
    }
}
