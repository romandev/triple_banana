// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import org.triple.banana.R;
import org.triple.banana.authentication.SecurityLevelChecker;
import org.triple.banana.authentication.SecurityLevelChecker.SecurityLevel;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;

public class PrivacyAndSecuritySettings extends PreferenceFragmentCompat {
    private @Nullable SwitchPreferenceCompat mSecureLogin;
    private @Nullable SwitchPreferenceCompat mBrowserLock;

    @Override
    public void onCreatePreferences(@NonNull Bundle savedInstanceState, @NonNull String rootKey) {
        addPreferencesFromResource(R.xml.privacy_and_security_preferences);
        mSecureLogin = findPreference(FeatureName.SECURE_LOGIN);
        mBrowserLock = findPreference(FeatureName.BROWSER_LOCK);
    }

    @Override
    public void onActivityCreated(@NonNull Bundle savedInstanceState) {
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

    private void onSecurityLevelChanged(@NonNull SecurityLevel newLevel) {
        boolean isSecure = newLevel == SecurityLevel.SECURE;
        if (mSecureLogin != null) {
            mSecureLogin.setEnabled(isSecure);
            if (!isSecure) mSecureLogin.setChecked(false);
        }
        if (mBrowserLock != null) {
            mBrowserLock.setEnabled(isSecure);
            if (!isSecure) mBrowserLock.setChecked(false);
        }
    }
}
