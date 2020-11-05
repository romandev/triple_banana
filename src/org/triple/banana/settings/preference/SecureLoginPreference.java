// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import org.banana.cake.interfaces.BananaSwitchPreference;
import org.triple.banana.authentication.Authenticator;

public class SecureLoginPreference extends BananaSwitchPreference {
    public SecureLoginPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnPreferenceChangeListener((preference, newValue) -> {
            if (isChecked()) {
                Authenticator.get().authenticate(result -> {
                    if (result) setChecked(false);
                });
                return false;
            }
            return true;
        });
    }
}
