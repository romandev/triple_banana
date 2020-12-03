// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;

import org.banana.cake.interfaces.BananaSecureDnsSettings;

public class SecureDnsPreference extends Preference {
    public SecureDnsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSummary(BananaSecureDnsSettings.get().getSummary(context));
    }
}
