// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaFeatureFlags;
import org.triple.banana.R;
import org.triple.banana.appmenu.AppMenuDelegate;
import org.triple.banana.toolbar.ToolbarEditor;

public class ExtensionFeatures extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.banana_extension_preferences);

        AppMenuDelegate.get().setNewFeatureIcon(false);

        final SwitchPreferenceCompat bottomToolbar =
                (SwitchPreferenceCompat) findPreference(FeatureName.BOTTOM_TOOLBAR);
        bottomToolbar.setOnPreferenceChangeListener((preference, newValue) -> {
            showRestartDialog();
            return true;
        });

        final Preference toolbarEditor = findPreference("launch_toolbar_editor");
        toolbarEditor.setOnPreferenceClickListener(preference -> {
            ToolbarEditor.show(getActivity());
            return false;
        });

        final SwitchPreferenceCompat mediaRemote =
                (SwitchPreferenceCompat) findPreference(FeatureName.MEDIA_REMOTE);
        mediaRemote.setOnPreferenceClickListener(preference -> {
            showRestartDialog();
            return true;
        });

        final SwitchPreferenceCompat autoPlay =
                (SwitchPreferenceCompat) findPreference(FeatureName.AUTOPLAY);
        autoPlay.setOnPreferenceClickListener(preference -> {
            showRestartDialog();
            return true;
        });

        final SwitchPreferenceCompat autoClearBrowsingData =
                (SwitchPreferenceCompat) findPreference(FeatureName.AUTO_CLEAR_BROWSING_DATA);
        autoClearBrowsingData.setOnPreferenceChangeListener((preference, newValue) -> {
            setEnabled(FeatureName.AUTO_CLEAR_BROWSING_DATA, (boolean) newValue);
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
        public static final String AUTOPLAY = "feature_name_autoplay";
        public static final String AUTO_CLEAR_BROWSING_DATA =
                "feature_name_auto_clear_browsing_data";
        public static final String BACKGROUND_PLAY = "feature_name_background_play";
        // bottom_toolbar_enabled preference value is referenced to enable bottom toolbar in
        // upstream. For sync with this value in triple banana, BOTTOM_TOOLBAR feature name has
        // diffrent name style like others.
        public static final String BOTTOM_TOOLBAR = "bottom_toolbar_enabled";
        public static final String EXTERNAL_DOWNLOAD_MANAGER =
                "feature_name_external_download_manager";
        public static final String SECURE_LOGIN = "feature_name_secure_login";
        public static final String SECURE_DNS = "feature_name_secure_dns";
        public static final String DARK_MODE = "feature_name_dark_mode";
        public static final String TRANSLATE = "feature_name_translate";
        public static final String BROWSER_LOCK = "feature_name_browser_lock";
        public static final String MEDIA_REMOTE = "feature_name_media_remote";
    }

    public static void setEnabled(String feature, boolean value) {
        if (TextUtils.equals(feature, FeatureName.ADBLOCK)) {
            BananaFeatureFlags.get().setAdblockEnabled(value);
            return;
        }

        if (TextUtils.equals(feature, FeatureName.TRANSLATE)) {
            BananaFeatureFlags.get().setTranslateEnabled(value);
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

    public static boolean wasSetByUser(String feature) {
        return BananaApplicationUtils.get().getSharedPreferences().contains(feature);
    }

    public static boolean isEnabled(String feature, boolean defaultValue) {
        if (TextUtils.equals(feature, FeatureName.ADBLOCK)) {
            return BananaFeatureFlags.get().isAdblockEnabled();
        }

        if (TextUtils.equals(feature, FeatureName.TRANSLATE)) {
            return BananaFeatureFlags.get().isTranslateEnabled();
        }

        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                feature, defaultValue);
    }
}
