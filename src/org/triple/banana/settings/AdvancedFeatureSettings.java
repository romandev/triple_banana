// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;
import org.triple.banana.settings.preference.AdblockPreference;
import org.triple.banana.settings.preference.LongClickablePreference;
import org.triple.banana.subresource_filter.RulesetLoader;
import org.triple.banana.subresource_filter.RulesetLoader.UpdateState;
import org.triple.banana.version.VersionInfo;

public class AdvancedFeatureSettings
        extends PreferenceFragmentCompat implements RulesetLoader.Listener {
    private static final @NonNull String PREF_UPDATE_ADBLOCK_FILTER = "update_adblock_filter";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getActivity().setTitle(R.string.advanced_features);
        addPreferencesFromResource(R.xml.advanced_features_preferences);

        RulesetLoader.instance.addListener(this);

        final LongClickablePreference updateAdblockFilterPreference =
                findPreference(PREF_UPDATE_ADBLOCK_FILTER);
        updateAdblockFilterPreference.setOnPreferenceClickListener(preference -> {
            RulesetLoader.instance.forceUpdateRemoteRuleset();
            return false;
        });
        updateAdblockFilterPreference.setOnLongClickListener(view -> {
            RulesetLoader.instance.forceUpdateRuleset();
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RulesetLoader.instance.removeListener(this);
    }

    @Override
    public void onStateChanged(@NonNull UpdateState state) {
        switch (state) {
            case VERSION_CHANGED:
                AdblockPreference adblockPreference = findPreference(FeatureName.ADBLOCK);
                adblockPreference.refresh();
                return;
            case ALREADY_LATEST:
                Toast.makeText(getContext(),
                             getContext().getResources().getString(R.string.already_latest_version),
                             Toast.LENGTH_LONG)
                        .show();
                return;
        }
    }
}
