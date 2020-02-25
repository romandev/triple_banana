// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.text.TextUtils;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaFeatureFlags;
import org.triple.banana.R;
import org.triple.banana.authentication.Authenticator;
import org.triple.banana.toolbar.ToolbarEditor;

public class ExtensionFeatures extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.banana_extension_preferences);
        Preference toolbarEditor = findPreference("launch_toolbar_editor");
        toolbarEditor.setOnPreferenceClickListener(preference -> {
            ToolbarEditor.show(getActivity());
            return false;
        });

        SwitchPreferenceCompat adblock =
                (SwitchPreferenceCompat) findPreference(FeatureName.ADBLOCK);
        adblock.setChecked(isEnabled(FeatureName.ADBLOCK));
        adblock.setOnPreferenceChangeListener((preference, newValue) -> {
            setEnabled(FeatureName.ADBLOCK, (boolean) newValue);
            return true;
        });

        SwitchPreferenceCompat safeLogin =
                (SwitchPreferenceCompat) findPreference(FeatureName.SAFE_LOGIN);
        safeLogin.setOnPreferenceChangeListener((preference, newValue) -> {
            if (safeLogin.isChecked()) {
                Authenticator.get().authenticate(result -> {
                    if (result) safeLogin.setChecked(false);
                });
                return false;
            }
            return true;
        });

        SwitchPreferenceCompat backgroundPlay =
                (SwitchPreferenceCompat) findPreference(FeatureName.BACKGROUND_PLAY);
        backgroundPlay.setOnPreferenceChangeListener((preference, newValue) -> {
            showRestartDialog();
            return true;
        });

        SwitchPreferenceCompat bottomToolbar =
                (SwitchPreferenceCompat) findPreference(FeatureName.BOTTOM_TOOLBAR);
        bottomToolbar.setOnPreferenceChangeListener((preference, newValue) -> {
            showRestartDialog();
            return true;
        });
    }

    private void showRestartDialog() {
        AlertDialog.Builder builder = BananaApplicationUtils.get().getDialogBuilder(getActivity());
        builder.setMessage(R.string.restart_message)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> { BananaApplicationUtils.get().restart(); })
                .create()
                .show();
    }

    public static class FeatureName {
        public static final String ADBLOCK = "feature_name_adblock";
        public static final String BACKGROUND_PLAY = "feature_name_background_play";
        // bottom_toolbar_enabled preference value is referenced to enable bottom toolbar in
        // upstream. For sync with this value in triple banana, BOTTOM_TOOLBAR feature name has
        // diffrent name style like others.
        public static final String BOTTOM_TOOLBAR = "bottom_toolbar_enabled";
        public static final String SAFE_LOGIN = "feature_name_safe_login";
    }

    public static void setEnabled(String feature, boolean value) {
        if (TextUtils.equals(feature, FeatureName.ADBLOCK)) {
            BananaFeatureFlags.get().setAdblockEnabled(value);
            return;
        }

        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean(feature, value);
        editor.apply();
    }

    public static boolean isEnabled(String feature) {
        return isEnabled(feature, false);
    }

    public static boolean isEnabled(String feature, boolean defaultValue) {
        if (TextUtils.equals(feature, FeatureName.ADBLOCK)) {
            return BananaFeatureFlags.get().isAdblockEnabled();
        }

        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                feature, defaultValue);
    }
}
