// Copyright 2020 The Triple Banana Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.triple.banana.settings;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import org.triple.banana.R;
import org.triple.banana.toolbar.ToolbarEditor;

public class ExtensionFeaturesSettings extends PreferenceFragmentCompat {
    private Preference mLaunchToolbarEditor;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.banana_extension_preferences);
        mLaunchToolbarEditor = findPreference("launch_toolbar_editor");
        mLaunchToolbarEditor.setOnPreferenceClickListener(preference -> {
            ToolbarEditor.show(getActivity());
            return false;
        });
    }
}
