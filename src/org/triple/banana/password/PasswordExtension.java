// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.package org.triple.banana;

package org.triple.banana.password;

import android.content.Context;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;

import org.banana.cake.interfaces.BananaPasswordExtension;
import org.triple.banana.R;
import org.triple.banana.authentication.AuthenticationManagerImpl;
import org.triple.banana.authentication.mojom.AuthenticationManager;

public class PasswordExtension implements BananaPasswordExtension {
    public static final String PREF_AUTHENTICATION_SWITCH = "authentication_switch";
    private static final int ORDER_SWITCH = 0;
    private static AuthenticationManager authenticationManager;

    private SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
        if (authenticationManager == null)
            authenticationManager = new AuthenticationManagerImpl.Factory().createImpl();
        SwitchPreferenceCompat authenticationSwitch = new SwitchPreferenceCompat(context, null);
        authenticationSwitch.setKey(PREF_AUTHENTICATION_SWITCH);
        authenticationSwitch.setOrder(ORDER_SWITCH);
        authenticationSwitch.setDefaultValue(false);
        authenticationSwitch.setTitle(
                context.getResources().getString(R.string.prefs_authentication));
        authenticationSwitch.setSummaryOn(context.getResources().getString(R.string.text_on));
        authenticationSwitch.setSummaryOff(context.getResources().getString(R.string.text_off));
        authenticationSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if (authenticationSwitch.isChecked()) {
                authenticationManager.authenticate((result) -> {
                    if (result) {
                        authenticationSwitch.setChecked((boolean) newValue);
                    }
                });
                return false;
            } else {
                return true;
            }
        });
        return authenticationSwitch;
    }

    @Override
    public void overridePreferenceScreen(Context context, PreferenceScreen screen) {
        screen.addPreference(createAuthenticationSwitch(context));
    }
}
