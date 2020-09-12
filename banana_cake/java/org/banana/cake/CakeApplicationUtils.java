// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.banana.cake;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

import org.banana.cake.interfaces.BananaApplicationUtils;

import org.chromium.base.ApplicationStatus;
import org.chromium.base.ContextUtils;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.ApplicationLifetime;
import org.chromium.chrome.browser.customtabs.CustomTabActivity;
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
    public boolean hasVisibleActivities() {
        return ApplicationStatus.hasVisibleActivities();
    }

    @Override
    public void restart() {
        ApplicationLifetime.terminate(true);
    }

    @Override
    public void shutdown() {
        ApplicationLifetime.terminate(false);
    }

    @Override
    public AlertDialog.Builder getDialogBuilder(Context context) {
        return new UiUtils.CompatibleAlertDialogBuilder(
                context, R.style.Theme_Chromium_AlertDialog);
    }

    @Override
    public void showInfoPage(String url) {
        CustomTabActivity.showInfoPage(getApplicationContext(), url);
    }
}
