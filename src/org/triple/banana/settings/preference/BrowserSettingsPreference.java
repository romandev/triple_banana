// Copyright 2021 The Triple Banana Authors. All rights reserved.
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

class BrowserSettingsPreference extends Preference {
    public BrowserSettingsPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceClickListener(v -> {
            BananaApplicationUtils.get().openBrowserSettings(getContext());
            return true;
        });
    }
}
