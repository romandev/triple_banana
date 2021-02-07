// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.application;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaClearBrowsingData;
import org.banana.cake.interfaces.BananaToolbarManager;
import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class ApplicationLifeTime {
    private static final @NonNull String WAS_ALWAYS_CLICKED =
            "application_life_time_was_always_clicked";
    private static final @NonNull String WAS_AUTO_CLEAR_CHECKBOX_ENABLED =
            "application_life_time_was_auto_clear_checkbox_enabled";

    public void terminate() {
        boolean shouldClearData = isAutoClearFeatureEnabled();
        if (shouldClearData || wasAlwaysClicked()) {
            terminateInternal(shouldClearData);
            return;
        }

        Activity activity = BananaApplicationUtils.get().getLastTrackedFocusedActivity();
        if (activity == null) {
            terminateInternal(shouldClearData);
            return;
        }

        showAutoClearSuggestionDialog(activity);
    }

    private void showAutoClearSuggestionDialog(@NonNull Activity activity) {
        final View dialogContent =
                activity.getLayoutInflater().inflate(R.layout.auto_clear_suggestion, null);
        final CheckBox autoClearCheckbox = dialogContent.findViewById(R.id.auto_clear_checkbox);
        autoClearCheckbox.setChecked(wasAutoClearCheckboxEnabled());

        BananaApplicationUtils.get()
                .getDialogBuilder(activity)
                .setTitle(activity.getResources().getString(R.string.exit_banana_browser))
                .setView(dialogContent)
                .setNeutralButton(android.R.string.cancel,
                        (dialogInterface, which) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.just_once,
                        (dialogInterface, which) -> {
                            setAutoClearCheckboxCache(autoClearCheckbox.isChecked());
                            terminateInternal(autoClearCheckbox.isChecked());
                        })
                .setNegativeButton(R.string.always,
                        (dialogInterface, which) -> {
                            if (autoClearCheckbox.isChecked()) {
                                enableAutoClearFeature();
                            }
                            setAlwaysClicked();
                            terminateInternal(autoClearCheckbox.isChecked());
                        })
                .show();
    }

    private void terminateInternal(boolean shouldClearData) {
        if (shouldClearData) {
            BananaClearBrowsingData.get().clearBrowsingData(BananaToolbarManager.get()::terminate);
        } else {
            BananaToolbarManager.get().terminate();
        }
    }

    private boolean isAutoClearFeatureEnabled() {
        return ExtensionFeatures.isEnabled(FeatureName.AUTO_CLEAR_BROWSING_DATA);
    }

    private boolean wasAlwaysClicked() {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                WAS_ALWAYS_CLICKED, false);
    }

    private boolean wasAutoClearCheckboxEnabled() {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                WAS_AUTO_CLEAR_CHECKBOX_ENABLED, false);
    }

    private void enableAutoClearFeature() {
        ExtensionFeatures.setEnabled(FeatureName.AUTO_CLEAR_BROWSING_DATA, true);
    }

    private void setAlwaysClicked() {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean(WAS_ALWAYS_CLICKED, true);
        editor.apply();
    }

    private void setAutoClearCheckboxCache(boolean enabled) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean(WAS_AUTO_CLEAR_CHECKBOX_ENABLED, enabled);
        editor.apply();
    }
}
