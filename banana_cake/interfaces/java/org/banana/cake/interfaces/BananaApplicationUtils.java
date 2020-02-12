// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import org.chromium.base.ActivityState;
import org.chromium.base.ApplicationStatus.ActivityStateListener;

public interface BananaApplicationUtils {
    public interface BananaActivityState extends ActivityState {}
    public interface BananaActivityStateListener extends ActivityStateListener {}

    static BananaApplicationUtils get() {
        return BananaInterfaceProvider.get(BananaApplicationUtils.class);
    }

    Context getApplicationContext();
    SharedPreferences getSharedPreferences();
    void registerStateListenerForAllActivities(BananaActivityStateListener listener);
    void unregisterActivityStateListener(BananaActivityStateListener listener);
}
