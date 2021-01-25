// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import org.banana.cake.interfaces.BananaApplicationUtils;
import org.banana.cake.interfaces.BananaSwitchPreference;

class RestartSwitchPreference extends BananaSwitchPreference {
    protected RestartSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener(null);
    }

    @Override
    public void setOnPreferenceChangeListener(
            final @Nullable Preference.OnPreferenceChangeListener listener) {
        super.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean isValueChanged =
                    ensureNonNull(listener).onPreferenceChange(preference, newValue);
            if (isValueChanged) {
                BananaApplicationUtils.get().showRestartDialog(getContext());
            }
            return isValueChanged;
        });
    }

    private @NonNull OnPreferenceChangeListener ensureNonNull(
            @Nullable OnPreferenceChangeListener listener) {
        if (listener == null) {
            listener = (preference, newValue) -> {
                return true;
            };
        }
        return listener;
    }
}
