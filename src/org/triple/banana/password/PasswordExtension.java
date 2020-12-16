// Copyright 2019 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.password;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import org.banana.cake.interfaces.BananaPasswordExtension;
import org.triple.banana.R;
import org.triple.banana.authentication.Authenticator;
import org.triple.banana.authentication.SecurityLevelChecker;
import org.triple.banana.authentication.SecurityLevelChecker.SecurityLevel;
import org.triple.banana.settings.ExtensionFeatures;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

import java.lang.ref.WeakReference;

public class PasswordExtension implements BananaPasswordExtension {
    private SecurityLevel mCurrentSecurityLevel = SecurityLevel.UNKNOWN;
    private boolean mSecureLoginSwitchChecked;
    private WeakReference<SwitchCompat> mInfobarSwitch;
    private WeakReference<TextView> mInfobarSwitchDescription;

    public PasswordExtension() {
        SecurityLevelChecker.get().addListener(this::onSecurityLevelChanged);
    }

    private SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
        SwitchPreferenceCompat authenticationSwitch = new SwitchPreferenceCompat(context, null);
        authenticationSwitch.setKey(FeatureName.SECURE_LOGIN);
        authenticationSwitch.setOrder(0);
        authenticationSwitch.setTitle(
                context.getResources().getString(R.string.authentication_title));
        authenticationSwitch.setSummaryOn(context.getResources().getString(R.string.text_on));
        authenticationSwitch.setSummaryOff(context.getResources().getString(R.string.text_off));
        authenticationSwitch.setEnabled(mCurrentSecurityLevel == SecurityLevel.SECURE);
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

    private void updateSecureLoginSwitchState(boolean enabled) {
        if (mInfobarSwitch == null || mInfobarSwitch.get() == null) return;

        mInfobarSwitch.get().setEnabled(enabled);
        mInfobarSwitch.get().setChecked(enabled);
        mInfobarSwitchDescription.get().setEnabled(enabled);
    }

    @Override
    public void overridePreferenceScreen(Context context, PreferenceScreen screen) {
        screen.addPreference(createAuthenticationSwitch(context));
    }

    @Override
    public void setupSecureLoginSwitch(View container) {
        assert !isSecureLoginEnabled();

        mSecureLoginSwitchChecked = mCurrentSecurityLevel == SecurityLevel.SECURE;

        SwitchCompat switchView = container.findViewById(R.id.password_infobar_use_authentication);
        mInfobarSwitch = new WeakReference<>(switchView);
        switchView.setOnCheckedChangeListener(
                (buttonView, isChecked) -> { mSecureLoginSwitchChecked = isChecked; });

        TextView text = (TextView) container.findViewById(R.id.control_message);
        mInfobarSwitchDescription = new WeakReference<>(text);
        text.setText(R.string.use_secure_login);

        updateSecureLoginSwitchState(mSecureLoginSwitchChecked);
    }

    @Override
    public boolean isSecureLoginEnabled() {
        return mCurrentSecurityLevel == SecurityLevel.SECURE
                && ExtensionFeatures.isEnabled(FeatureName.SECURE_LOGIN);
    }

    @Override
    public void setSecureLoginEnabled() {
        if (mCurrentSecurityLevel == SecurityLevel.NON_SECURE
                || ExtensionFeatures.isEnabled(FeatureName.SECURE_LOGIN)) {
            return;
        }
        ExtensionFeatures.setEnabled(FeatureName.SECURE_LOGIN, mSecureLoginSwitchChecked);
    }

    public void onSecurityLevelChanged(SecurityLevel newLevel) {
        mCurrentSecurityLevel = newLevel;
        if (ExtensionFeatures.isEnabled(FeatureName.SECURE_LOGIN)
                && newLevel == SecurityLevel.NON_SECURE) {
            ExtensionFeatures.setEnabled(FeatureName.SECURE_LOGIN, false);
        }

        updateSecureLoginSwitchState(newLevel == SecurityLevel.SECURE);
    }
}
