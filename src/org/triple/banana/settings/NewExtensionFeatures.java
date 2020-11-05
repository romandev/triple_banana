// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import org.triple.banana.R;

public class NewExtensionFeatures extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(@NonNull Bundle savedInstanceState, @NonNull String rootKey) {
        addPreferencesFromResource(R.xml.new_banana_extension_preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().removeAll();
        addPreferencesFromResource(R.xml.new_banana_extension_preferences);
    }
}
