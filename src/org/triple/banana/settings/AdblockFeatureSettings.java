// Copyright 2021 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.settings;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.triple.banana.R;
import org.triple.banana.settings.ExtensionFeatures.FeatureName;
import org.triple.banana.settings.preference.AdblockPreference;
import org.triple.banana.settings.preference.LongClickablePreference;
import org.triple.banana.subresource_filter.RulesetLoader;
import org.triple.banana.subresource_filter.RulesetLoader.UpdateState;

public class AdblockFeatureSettings
        extends PreferenceFragmentCompat implements RulesetLoader.Listener {
    private static final @NonNull String PREF_UPDATE_ADBLOCK_FILTER = "update_adblock_filter";
    private static final @NonNull String PREF_RESET_ADBLOCK_FILTER = "reset_adblock_filter";
    private @Nullable Toast mToast;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        getActivity().setTitle(R.string.adblock);
        addPreferencesFromResource(R.xml.adblock_feature_preferences);

        RulesetLoader.instance.addListener(this);

        final Preference updateFilterPreference = findPreference(PREF_UPDATE_ADBLOCK_FILTER);
        updateFilterPreference.setOnPreferenceClickListener(preference -> {
            RulesetLoader.instance.forceUpdateRemoteRuleset();
            return false;
        });

        final LongClickablePreference resetFilterPreference =
                findPreference(PREF_RESET_ADBLOCK_FILTER);
        resetFilterPreference.setOnPreferenceClickListener(preference -> {
            showToast(R.string.please_long_click_to_reset_filter);
            return false;
        });
        resetFilterPreference.setOnLongClickListener(view -> {
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
                showToast(R.string.already_latest_version);
                return;
        }
    }

    private void showToast(@StringRes int message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(
                getContext(), getContext().getResources().getString(message), Toast.LENGTH_LONG);
        mToast.show();
    }
}
