// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake.interfaces;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

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
    boolean hasVisibleActivities();
    void restart();
    AlertDialog.Builder getDialogBuilder(Context context);
    void showInfoPage(String url);
}
