// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.banana.cake;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaApplicationUtils.BananaActivityStateListener;

import org.chromium.base.ApplicationStatus;
import org.chromium.base.ContextUtils;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.ApplicationLifetime;
import org.chromium.ui.UiUtils;

class CakeApplicationUtils implements BananaApplicationUtils {
    @Override
    public Context getApplicationContext() {
        return ContextUtils.getApplicationContext();
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return getApplicationContext().getSharedPreferences(
                "org.triple.banana_preferences", Context.MODE_PRIVATE);
    }

    @Override
    public void registerStateListenerForAllActivities(BananaActivityStateListener listener) {
        ApplicationStatus.registerStateListenerForAllActivities(listener);
    }

    @Override
    public void unregisterActivityStateListener(BananaActivityStateListener listener) {
        ApplicationStatus.unregisterActivityStateListener(listener);
    }

    @Override
    public void restart() {
        ApplicationLifetime.terminate(true);
    }

    @Override
    public AlertDialog.Builder getDialogBuilder(Context context) {
        return new UiUtils.CompatibleAlertDialogBuilder(
                context, R.style.Theme_Chromium_AlertDialog);
    }
}
