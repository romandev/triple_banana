// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;
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
    }

    public static class FeatureName {
        public static final String SAFE_LOGIN = "feature_name_safe_login";
    }

    public static void setEnabled(String feature, boolean value) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean(feature, value);
        editor.apply();
    }

    public static boolean isEnabled(String feature) {
        return isEnabled(feature, false);
    }

    public static boolean isEnabled(String feature, boolean defaultValue) {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                feature, defaultValue);
    }
}
