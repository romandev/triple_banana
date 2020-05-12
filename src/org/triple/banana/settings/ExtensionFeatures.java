// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

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
import org.triple.banana.appmenu.AppMenuDelegate;
import org.triple.banana.authentication.Authenticator;
import org.triple.banana.authentication.SecurityLevelChecker;
import org.triple.banana.authentication.SecurityLevelChecker.SecurityLevel;
import org.triple.banana.remote_config.RemoteConfig;
import org.triple.banana.secure_dns.SecureDnsNotificationManager;
import org.triple.banana.theme.DarkModeController;
import org.triple.banana.toolbar.ToolbarEditor;

public class ExtensionFeatures extends PreferenceFragmentCompat {
    private SwitchPreferenceCompat mSecureLogin;
    private RemoteConfig mRemoteConfig =
            new RemoteConfig("https://zino.dev/triple_banana_config/remote_config.json");

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.banana_extension_preferences);

        AppMenuDelegate.get().setNewFeatureIcon(false);

        final SwitchPreferenceCompat adblock =
                (SwitchPreferenceCompat) findPreference(FeatureName.ADBLOCK);
        adblock.setChecked(isEnabled(FeatureName.ADBLOCK));
        adblock.setOnPreferenceChangeListener((preference, newValue) -> {
            setEnabled(FeatureName.ADBLOCK, (boolean) newValue);
            return true;
        });

        mSecureLogin = (SwitchPreferenceCompat) findPreference(FeatureName.SECURE_LOGIN);
        mSecureLogin.setOnPreferenceChangeListener((preference, newValue) -> {
            if (mSecureLogin.isChecked()) {
                Authenticator.get().authenticate(result -> {
                    if (result) mSecureLogin.setChecked(false);
                });
                return false;
            }
            return true;
        });

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

        final SwitchPreferenceCompat darkMode =
                (SwitchPreferenceCompat) findPreference(FeatureName.DARK_MODE);
        darkMode.setChecked(DarkModeController.get().isDarkModeOn());
        darkMode.setOnPreferenceChangeListener((preference, newValue) -> {
            DarkModeController.get().toggle();
            return true;
        });

        final SwitchPreferenceCompat backgroundPlay =
                (SwitchPreferenceCompat) findPreference(FeatureName.BACKGROUND_PLAY);
        boolean isVisible = wasSetByUser(FeatureName.BACKGROUND_PLAY);
        backgroundPlay.setVisible(isVisible);
        backgroundPlay.setOnPreferenceChangeListener((preference, newValue) -> {
            showRestartDialog();
            return true;
        });

        if (!isVisible) {
            mRemoteConfig.getAsync(config -> {
                boolean isRemoteEnabled = config.optBoolean("background_play");
                if (isRemoteEnabled && backgroundPlay != null) {
                    setEnabled(FeatureName.BACKGROUND_PLAY, backgroundPlay.isChecked());
                    backgroundPlay.setVisible(true);
                }
            });
        }

        final SwitchPreferenceCompat secureDNS =
                (SwitchPreferenceCompat) findPreference(FeatureName.SECURE_DNS);
        secureDNS.setChecked(isEnabled(FeatureName.SECURE_DNS));
        secureDNS.setOnPreferenceChangeListener((preference, newValue) -> {
            showRestartDialog();
            if (((boolean) newValue)
                    && SecureDnsNotificationManager.getInstance().wasNotificationShown()) {
                SecureDnsNotificationManager.getInstance().resetNotificationState();
            }
            return true;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityLevelChecker.get().addListener(this::onSecurityLevelChanged);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // FIXME(#313): Need a good way to prevent memory leak. Because 'onDestroy' is not
        // guaranteed to be called in the fragment.
        SecurityLevelChecker.get().removeListener(this::onSecurityLevelChanged);
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

    public void onSecurityLevelChanged(SecurityLevel newLevel) {
        if (mSecureLogin != null) {
            boolean isSecure = newLevel == SecurityLevel.SECURE;
            mSecureLogin.setEnabled(isSecure);
            if (!isSecure) {
                mSecureLogin.setChecked(false);
            }
        }
    }

    public static class FeatureName {
        public static final String ADBLOCK = "feature_name_adblock";
        public static final String BACKGROUND_PLAY = "feature_name_background_play";
        // bottom_toolbar_enabled preference value is referenced to enable bottom toolbar in
        // upstream. For sync with this value in triple banana, BOTTOM_TOOLBAR feature name has
        // diffrent name style like others.
        public static final String BOTTOM_TOOLBAR = "bottom_toolbar_enabled";
        public static final String SECURE_LOGIN = "feature_name_secure_login";
        public static final String SECURE_DNS = "feature_name_secure_dns";
        public static final String DARK_MODE = "feature_name_dark_mode";
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

    public static boolean wasSetByUser(String feature) {
        return BananaApplicationUtils.get().getSharedPreferences().contains(feature);
    }

    public static boolean isEnabled(String feature, boolean defaultValue) {
        if (TextUtils.equals(feature, FeatureName.ADBLOCK)) {
            return BananaFeatureFlags.get().isAdblockEnabled();
        }

        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                feature, defaultValue);
    }
}
