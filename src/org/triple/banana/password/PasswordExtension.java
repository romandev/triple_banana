// Copyright 2019 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.package org.triple.banana;

package org.triple.banana.password;

import android.content.Context;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

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
    private boolean mSafeLoginSwitchChecked;
    private WeakReference<SwitchCompat> mInfobarSwitch;
    private WeakReference<TextView> mInfobarSwitchDescription;

    public PasswordExtension() {
        SecurityLevelChecker.get().addListener(this::onSecurityLevelChanged);
    }

    private SwitchPreferenceCompat createAuthenticationSwitch(Context context) {
        SwitchPreferenceCompat authenticationSwitch = new SwitchPreferenceCompat(context, null);
        authenticationSwitch.setKey(FeatureName.SAFE_LOGIN);
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

    private void updateSafeLoginSwitchState(boolean enabled) {
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
    public void setupSafeLoginSwitch(View container) {
        assert !isSafeLoginEnabled();

        mSafeLoginSwitchChecked = mCurrentSecurityLevel == SecurityLevel.SECURE;

        SwitchCompat switchView = container.findViewById(R.id.infobar_extra_check);
        mInfobarSwitch = new WeakReference<>(switchView);
        switchView.setOnCheckedChangeListener(
                (buttonView, isChecked) -> { mSafeLoginSwitchChecked = isChecked; });

        TextView text = (TextView) container.findViewById(R.id.control_message);
        mInfobarSwitchDescription = new WeakReference<>(text);
        text.setText(R.string.use_safe_login);

        updateSafeLoginSwitchState(mSafeLoginSwitchChecked);
    }

    @Override
    public boolean isSafeLoginEnabled() {
        return mCurrentSecurityLevel == SecurityLevel.SECURE
                && ExtensionFeatures.isEnabled(FeatureName.SAFE_LOGIN);
    }

    @Override
    public void setSafeLoginEnabled() {
        if (mCurrentSecurityLevel == SecurityLevel.NON_SECURE
                || ExtensionFeatures.isEnabled(FeatureName.SAFE_LOGIN)) {
            return;
        }
        ExtensionFeatures.setEnabled(FeatureName.SAFE_LOGIN, mSafeLoginSwitchChecked);
    }

    public void onSecurityLevelChanged(SecurityLevel newLevel) {
        mCurrentSecurityLevel = newLevel;
        if (ExtensionFeatures.isEnabled(FeatureName.SAFE_LOGIN)
                && newLevel == SecurityLevel.NON_SECURE) {
            ExtensionFeatures.setEnabled(FeatureName.SAFE_LOGIN, false);
        }

        updateSafeLoginSwitchState(newLevel == SecurityLevel.SECURE);
    }
}
