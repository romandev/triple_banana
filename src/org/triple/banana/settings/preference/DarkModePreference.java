// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import org.banana.cake.interfaces.BananaSwitchPreference;
import org.triple.banana.theme.DarkModeController;

public class DarkModePreference extends BananaSwitchPreference {
    public DarkModePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener((preference, newValue) -> {
            DarkModeController.get().toggle();
            return true;
        });
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        super.onSetInitialValue(defaultValue);
        setChecked(DarkModeController.get().isDarkModeOn());
    }
}
