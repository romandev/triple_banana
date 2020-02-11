// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.authentication;

import org.triple.banana.authentication.Authenticator.Callback;
import org.triple.banana.base.InterActivity;

class ActivityBasedBackend implements Backend {
    Class<? extends BaseActivity> mActivityClass;

    ActivityBasedBackend(Class<? extends BaseActivity> activityClass) {
        mActivityClass = activityClass;
    }

    @Override
    public void authenticate(Callback callback) {
        InterActivity.start(mActivityClass, new Object(), callback::onResult);
    }
}
