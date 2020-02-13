// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.package org.triple.banana;

package org.triple.banana.password;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaPasswordExtension;
import org.triple.banana.R;
import org.triple.banana.authentication.Authenticator;
import org.triple.banana.authentication.SecurityLevelChecker.SecurityLevel;

public class PasswordExtension implements BananaPasswordExtension {
    private static final String PREF_KEY_IS_SAFE_LOGIN_ENABLED = "is_safe_login_enabled";
    private static SecurityLevel sCurrentSecurityLevel = SecurityLevel.UNKNOWN;

    private SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
        SwitchPreferenceCompat authenticationSwitch = new SwitchPreferenceCompat(context, null);
        authenticationSwitch.setKey(PREF_KEY_IS_SAFE_LOGIN_ENABLED);
        authenticationSwitch.setOrder(0);
        authenticationSwitch.setTitle(
                context.getResources().getString(R.string.prefs_authentication));
        authenticationSwitch.setSummaryOn(context.getResources().getString(R.string.text_on));
        authenticationSwitch.setSummaryOff(context.getResources().getString(R.string.text_off));
        authenticationSwitch.setEnabled(sCurrentSecurityLevel == SecurityLevel.SECURE);
        authenticationSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if (authenticationSwitch.isChecked()) {
                Authenticator.get().authenticate(result -> {
                    if (result) authenticationSwitch.setChecked(false);
                });
                return false;
            }

            return true;
        });
        return authenticationSwitch;
    }

    @Override
    public void overridePreferenceScreen(Context context, PreferenceScreen screen) {
        screen.addPreference(createAuthenticationSwitch(context));
    }

    public static void onSecurityLevelChanged(SecurityLevel newLevel) {
        sCurrentSecurityLevel = newLevel;
        if (isAuthenticatorEnabled() && newLevel == SecurityLevel.NON_SECURE) {
            setAuthenticatorEnabled(false);
        }
    }

    private static boolean isAuthenticatorEnabled() {
        return BananaApplicationUtils.get().getSharedPreferences().getBoolean(
                PREF_KEY_IS_SAFE_LOGIN_ENABLED, false);
    }

    private static void setAuthenticatorEnabled(boolean value) {
        SharedPreferences.Editor editor =
                BananaApplicationUtils.get().getSharedPreferences().edit();
        editor.putBoolean(PREF_KEY_IS_SAFE_LOGIN_ENABLED, value);
        editor.apply();
    }
}
